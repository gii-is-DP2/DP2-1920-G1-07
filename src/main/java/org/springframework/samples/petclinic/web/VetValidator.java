
package org.springframework.samples.petclinic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VetValidator implements Validator {

	@Autowired
	private VetService vetService;


	public VetValidator(final VetService vetService) {
		this.vetService = vetService;
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		Vet vet = (Vet) obj;
		List<Specialty> specialties = vet.getSpecialties();
		String username = vet.getUser().getUsername();
		String password = vet.getUser().getPassword();

		if (specialties == null) {
			errors.rejectValue("specialties", "The specialties is empty", "The specialties is empty");
		}

		if (username == null || username == "") {
			errors.rejectValue("user.username", "The username is empty", "The username is empty");
		}

		if (password == null || password == "") {
			errors.rejectValue("user.password", "The password is empty", "The password is empty");
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Room.class.isAssignableFrom(clazz);

	}

}
