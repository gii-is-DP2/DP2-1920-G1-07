package org.springframework.samples.petclinic.web;

import org.springframework.validation.Validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.validation.Errors;


public class ReservationValidator implements Validator{
	
	private static final String REQUIERED = "This field is required";
	
	@Autowired
	private PetService petService;
	
	
	public ReservationValidator(PetService petService) {
		this.petService = petService;
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Reservation reser = (Reservation) obj;
		LocalDate now = LocalDate.now();
		LocalDate entryDate=reser.getEntryDate();
		LocalDate exitDate= reser.getExitDate();
		String pet = reser.getPet();
		Pet p = this.petService.findPetById(Integer.parseInt(pet));
		//DATE VALIDATION
		Boolean ambasNull = entryDate == null || exitDate == null;
		if(entryDate == null) {
			errors.rejectValue("entryDate",REQUIERED,REQUIERED);
		}
		if(exitDate == null) {
			errors.rejectValue("exitDate",REQUIERED,REQUIERED);
		}
		if(ambasNull.equals(false)) {
			if(entryDate.isBefore(now) ) {
					errors.rejectValue("entryDate", "The entry date must be after the current date","The entry date must be after the current date");
				}
		
					if(exitDate.isBefore(now) || exitDate.isBefore(entryDate)) {
						errors.rejectValue("exitDate", "The exit date must be after the current date and the entry date","The exit date must be after the current date and the entry date");
				}
			}
	
		if(pet == null) {
			errors.rejectValue("pet",REQUIERED,REQUIERED);
		}
		if(reser.getRoom().getType() != p.getType()) {
			errors.rejectValue("pet","This room cannot accommodate "+p.getType().getName().toUpperCase()+"S, it can only accommodate "+reser.getRoom().getType().getName().toUpperCase()+"S.","This room cannot accommodate "+p.getType().getName().toUpperCase()+"S, it can only accommodate "+reser.getRoom().getType().getName().toUpperCase()+"S.");
		}
		
		
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Reservation.class.isAssignableFrom(clazz);
		
	}

}
