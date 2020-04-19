package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RoomController {

	private static final String VIEWS_ROOM_CREATE_OR_UPDATE_FORM = "/rooms/createOrUpdateRoomForm"; 
	
	
	private final RoomService roomService;
	
	@Autowired
	private final PetService petService;
	
	@Autowired
	private final OwnerService ownerService;
	
	@Autowired
	private final ReservationService reservationService;
	
	private RoomValidator roomValidator;
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("reservation")
	public void initReservationBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("room")
	public void initRoomBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(roomValidator);
	}
	
	@Autowired
	public RoomController(final RoomService roomService,final PetService petService, final OwnerService ownerService, final ReservationService reservationService) {
		this.roomService = roomService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.reservationService = reservationService;
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
		RoomValidator roomValidator = new RoomValidator(roomService);
		roomValidator.validate(room, result);
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
		Room room = this.roomService.findRoomById(roomId);
		if(room != null && !room.getReservations().isEmpty()) {
			for(Reservation r : room.getReservations()) {
				if(!r.getStatus().getName().equals("ACCEPTED")) {
					roomService.delete(room);
					model.addAttribute("message", "Event Successfuly deleted");
				}
			}
		}else if(room != null && room.getReservations().isEmpty()){
			roomService.delete(room);
		}else {
			
			model.addAttribute("roomNotDeleted", "The room named "+room.getName()+" cannot be removed because it has reservations");
		}
		return view; 
	}
	
	@GetMapping(value = "/rooms/{roomId}/edit")
	public String processInitUpdateForm(@PathVariable("roomId") int roomId,ModelMap model) {
		Room room = this.roomService.findRoomById(roomId);
		model.addAttribute(room);
		return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/rooms/{roomId}/edit")
	public String processUpdateRoom(@Valid Room room, @PathVariable("roomId") int roomId,BindingResult result, ModelMap modelMap) {
		RoomValidator roomValidator = new RoomValidator(roomService,roomId);
		roomValidator.validate(room, result);
		
		if(result.hasErrors()) {
			return RoomController.VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		}else {
			room.setId(roomId);
			this.roomService.saveRoom(room);
			return "redirect:/rooms/"+room.getId();
		}
		
	}
	@GetMapping("/rooms/{roomId}")
	public ModelAndView showRoom(@PathVariable("roomId") int roomId,ModelMap model,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("rooms/roomDetails");
		
		Room r = this.roomService.findRoomById(roomId);
		if(r.getReservations() != null) {
		Integer numReservationsAccepted = (int) r.getReservations().stream().filter(x->x.getStatus().getName().equals("ACCEPTED")).count();
		Boolean completedRoom = numReservationsAccepted == r.getCapacity();
		model.addAttribute("completedRoom", completedRoom);
		//Para no poder eliminar una room si tiene reservas
		model.addAttribute("notHaveReservations", numReservationsAccepted==0);
		}
		//Para mostrar las reservas de cada usuario por cada habitacion, siempre y cuando no sea admin, ya que el admin las ve todas.
		if(!request.getUserPrincipal().getName().equals("admin1") && !request.getUserPrincipal().getName().equals("spring")) {
		Principal principal = request.getUserPrincipal();
		int ownerId = this.ownerService.findOwnerByUserName(principal.getName()).getId();
		Collection<Reservation> reservations = this.reservationService.findReservationsByOwnerAndRoomId(ownerId,roomId);
		model.addAttribute("myReservations", reservations);
		}
		model.put("room", this.roomService.findRoomById(roomId));
		mav.addObject(this.roomService.findRoomById(roomId));
		return mav;
	}
	
}