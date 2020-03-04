package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	
	@GetMapping()
	public String listadoDeRomms(ModelMap modelMap) { 
		String vista = "rooms/listadoRooms";
		Iterable<Room> rooms  = roomService.allRooms();
		modelMap.addAttribute("rooms", rooms);
		return vista;
	}	
}