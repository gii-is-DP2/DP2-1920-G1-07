
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DiagnosisValidator implements Validator {

	@Autowired
	private DiagnosisService diagnosisService;


	public DiagnosisValidator(final DiagnosisService diagnosisService) {
		this.diagnosisService = diagnosisService;
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		Diagnosis diagnosis = (Diagnosis) obj;
		String description = diagnosis.getDescription();
		LocalDate date = diagnosis.getDate();
		LocalDate now = LocalDate.now();

		if (description == null || description == "") {
			errors.rejectValue("description", "The description is empty", "The description is empty");
		}

		if (date != null) {
			Boolean posterior = date.isBefore(now);
			if (posterior) {
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
