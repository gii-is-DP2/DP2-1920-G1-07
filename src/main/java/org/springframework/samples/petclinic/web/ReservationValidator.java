package org.springframework.samples.petclinic.web;

import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


public class ReservationValidator implements Validator{
	
	private static final String REQUIERED = "This field is required";
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private RoomService roomService;
	
	
	@Override
	public void validate(Object obj, Errors errors) {
		Reservation reser = (Reservation) obj;
		LocalDate now = LocalDate.now();
		LocalDate entryDate=reser.getEntryDate();
		LocalDate exitDate= reser.getExitDate();
		String pet = reser.getPet();
		//DATE VALIDATION
		Boolean ambasNull = entryDate == null || exitDate == null;
		if(entryDate == null) {
			errors.rejectValue("entryDate",REQUIERED,REQUIERED);
		}
		if(exitDate == null) {
			errors.rejectValue("exitDate",REQUIERED,REQUIERED);
		}
		if(!ambasNull) {
			if(entryDate.isBefore(now) ) {
					errors.rejectValue("entryDate", "The entry date must be after the current date","The entry date must be after the current date");
				}
		
					if(exitDate.isBefore(now) || exitDate.isBefore(entryDate) || exitDate.equals(null)) {
						errors.rejectValue("exitDate", "The exit date must be after the current date and the entry date","The exit date must be after the current date and the entry date");
				}
			}
	
		if(pet == null) {
			errors.rejectValue("pet",REQUIERED,REQUIERED);
		
		}
		
		
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Reservation.class.isAssignableFrom(clazz);
		
	}

}
