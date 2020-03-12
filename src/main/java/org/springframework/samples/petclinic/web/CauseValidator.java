
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Cause;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CauseValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		//title, description, money,
		Cause cause = (Cause) obj;
		String title = cause.getTitle();
		String description = cause.getDescription();
		Double money = cause.getMoney();
		LocalDate deadline = cause.getDeadline();
		LocalDate now = LocalDate.now();

		if (title == null || title == "") {
			errors.rejectValue("title", "The title is empty", "The title is empty");
		}

		if (description == null || description == "") {
			errors.rejectValue("description", "The description is empty", "The description is empty");
		}

		if (money == null) {
			errors.rejectValue("money", "The money is empty", "The money is empty");
		} else if (money <= 0.) {
			errors.rejectValue("money", "The money must have a value greater than 0", "The money must have a value greater than 0");
		}

		if (deadline != null) {
			Boolean posterior = deadline.isBefore(now);
			if (posterior) {
				errors.rejectValue("deadline", "The deadline is before the current date", "The deadline is before the current date");
			}
		} else {
			errors.rejectValue("deadline", "The deadline is empty", "The deadline is empty");
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
