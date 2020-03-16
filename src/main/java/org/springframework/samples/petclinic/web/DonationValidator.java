package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DonationValidator implements Validator{


	@Override
	public void validate(final Object obj, final Errors errors) {
		Donation donation = (Donation) obj;
		Double money = donation.getMoney();

		
		if (money == null) {
			errors.rejectValue("money", "The money is empty", "The money is empty");
		} else if (money <= 0.) {
			errors.rejectValue("money", "The money must have a value greater than 0", "The money must have a value greater than 0");
		}
	}
	
	
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return Donation.class.isAssignableFrom(clazz);
	}
}
