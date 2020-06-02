package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RoomValidator implements Validator {

	private static final String			FIELD_REQUIRED	= "This field is required";
	@Autowired
	private RoomService roomService;
	
	private int roomId;
	
	public RoomValidator(RoomService roomService,int roomId) {
		this.roomService = roomService;
		this.roomId = roomId;
	}
	public RoomValidator(RoomService roomService) {
		this.roomService = roomService;
	}
	
	
	@Override
	public void validate(Object obj, Errors errors) {
		Room room = (Room) obj;
		if(room.getCapacity() != null && room.getCapacity() == 0) {
			errors.rejectValue("capacity", "The value 0 is not valid","The value 0 is not valid");
		}
		if(room.getCapacity() == null) {
			errors.rejectValue("capacity", FIELD_REQUIRED,FIELD_REQUIRED);
		}
		if(room.getType() == null) {
			errors.rejectValue("type", FIELD_REQUIRED,FIELD_REQUIRED);
		}
		
		Collection<String> roomNames = this.roomService.findAllRoomsName();
		if(this.roomId != 0) {
		Room roomToUpdate = this.roomService.findRoomById(roomId);
		roomNames.remove(roomToUpdate.getName());
		}
		if(roomNames.contains(room.getName())) {
			errors.rejectValue("name", "This value is already used","This value is already used");
		}
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Room.class.isAssignableFrom(clazz);
		
	}

}
