package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RoomController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "/rooms/createOrUpdateRoomForm"; 
	
	
	private final RoomService roomService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private AuthoritiesService	authoritiesService;
	
	@Autowired
	public RoomController(RoomService roomService, PetService petService) {
		this.roomService = roomService;
	}
	
	@GetMapping(value ="/rooms")
	public String listadoDeRomms(ModelMap modelMap) { 
		String vista = "rooms/roomList";
		Iterable<Room> rooms  = roomService.allRooms();
		modelMap.addAttribute("rooms", rooms);
		return vista;
	}
	
	@ModelAttribute("types")
	public Collection<PetType> allPetTypes() {
		Collection<PetType> petType = petService.findPetTypes();
		return petType;
	}
	

	@GetMapping(value ="/rooms/new")
	public String initCreationForm(ModelMap modelMap) {
		String view = "rooms/createOrUpdateRoomForm";
		Room room = new Room();
		modelMap.addAttribute("room", room);
		return view;
	}
	
	@PostMapping(value="/rooms/new")
	public String processSaveRoom(@Valid Room room, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("message", "Room not created");
			model.addAttribute("room", room);
			return "rooms/createOrUpdateRoomForm";
		}else { 
			roomService.saveRoom(room);
			model.addAttribute("message", "Room succesfully created");
		}
		return "redirect:/rooms/";
	}
	@GetMapping(value="/rooms/delete/{roomId}")
	public String processDeleteRoom(@PathVariable("roomId") int roomId,Model model) {
		String view = "redirect:/rooms/";
		Optional<Room> room = this.roomService.findRoomById(roomId);
		if(room.isPresent() && room.get().getReservations().isEmpty()) {
			roomService.delete(room.get());
			model.addAttribute("message", "Event Successfuly deleted");
		}else { 
			model.addAttribute("roomNotDeleted", "The room named "+room.get().getName()+" cannot be removed because it has reservations");
		}
		return view;
	}
	
	@GetMapping(value = "/rooms/{roomId}/edit")
	public String processInitUpdateForm(@PathVariable("roomId") int roomId,Model model) {
		Room room = this.roomService.findRoomById(roomId).get();
		model.addAttribute(room);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/rooms/{roomId}/edit" )
	public String processUpdateRoom(@Valid Room room, @PathVariable("roomId") int roomId,BindingResult result, ModelMap modelMap) {
		if(result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
			
		}else {
			room.setId(roomId);
			this.roomService.saveRoom(room);
			return "redirect:/rooms/"+room.getId();
		}
		
	}
	@GetMapping("/rooms/{roomId}")
	public ModelAndView showRoom(@PathVariable("roomId") int roomId,Model model,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("rooms/roomDetails");
		
		Room r = this.roomService.findRoomById(roomId).get();
		Integer numReservationsAccepted = (int) r.getReservations().stream().filter(x->x.getStatus().getName().equals("ACCEPTED")).count();
		Boolean completedRoom = numReservationsAccepted == r.getCapacity();
		model.addAttribute("completedRoom", completedRoom);
		//Para no poder eliminar una room si tiene reservas
		model.addAttribute("notHaveReservations", numReservationsAccepted==0);
		//Para mostrar las reservas de cada usuario, siempre y cuando no sea admin, ya que el admin las ve todas.
		if(!request.getUserPrincipal().getName().equals("admin1")) {
		Principal principal = request.getUserPrincipal();
		int ownerId = this.ownerService.findOwnerByUserName(principal.getName()).getId();
		Collection<Reservation> reservations = this.reservationService.findResrvationsByOwnerId(ownerId);
		model.addAttribute("myReservations", reservations);
		}
		
		mav.addObject(this.roomService.findRoomById(roomId).get());
		return mav;
	}
	
	@ModelAttribute("hasPermiss")
	public Boolean hasPermiss(HttpServletRequest request) {
		Boolean res = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for(GrantedAuthority a:auth.getAuthorities()) {
			res  = a.getAuthority().equals("admin");
		}
		return res;
	}
}