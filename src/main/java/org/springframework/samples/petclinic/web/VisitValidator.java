
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VisitValidator implements Validator {

	@Autowired
	private VisitService visitService;


	public VisitValidator(final VisitService visitService) {
		this.visitService = visitService;
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		Visit visit = (Visit) obj;
		String description = visit.getDescription();
		LocalDate date = visit.getDate();
		LocalDate now = LocalDate.now();

		if (description == null || description == "") {
			errors.rejectValue("description", "The description is empty", "The description is empty");
		}

		if (date != null) {
			if (date.isBefore(now)) {
				errors.rejectValue("date", "The date is before the current date", "The date is before the current date");
			}
		} else {
			errors.rejectValue("date", "The date is empty", "The date is empty");
		}

	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Cause.class.isAssignableFrom(clazz);
	}

}
