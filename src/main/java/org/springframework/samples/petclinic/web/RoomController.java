
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Sitter;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.samples.petclinic.service.SitterService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomController {

	private static final String			VIEWS_ROOM_CREATE_OR_UPDATE_FORM	= "/rooms/createOrUpdateRoomForm";
	private static final String 		MESSAGE = "message";
	private final RoomService			roomService;

	@Autowired
	private final PetService			petService;

	@Autowired
	private final OwnerService			ownerService;

	@Autowired
	private final ReservationService	reservationService;

	private RoomValidator				roomValidator;


	@InitBinder("pet")
	public void initPetBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("owner")
	public void initOwnerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("reservation")
	public void initReservationBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("room")
	public void initRoomBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(this.roomValidator);
	}


	@Autowired
	private final SitterService sitterService;


	@Autowired
	public RoomController(final RoomService roomService, final PetService petService, final OwnerService ownerService, final ReservationService reservationService, final SitterService sitterService) {
		this.roomService = roomService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.reservationService = reservationService;
		this.sitterService = sitterService;
	}

	@GetMapping(value = "/rooms")
	public String listadoDeRomms(final ModelMap modelMap) {
		String vista = "rooms/roomList";
		Iterable<Room> rooms = this.roomService.allRooms();
		modelMap.addAttribute("rooms", rooms);
		return vista;
	}

	@GetMapping(value = "sitter/rooms")
	public String listadoDeRommsSitter(final HttpServletRequest request, final ModelMap modelMap) {
		Principal principal = request.getUserPrincipal();
		String vista = "rooms/roomList";
		Iterable<Room> rooms = this.roomService.findRoomsBySitterUserName(principal.getName());
		modelMap.addAttribute("rooms", rooms);
		return vista;
	}
	@ModelAttribute("types")
	public Collection<PetType> allPetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping(value = "/rooms/{roomId}/sitter")
	public String initChangeSitter(@PathVariable("roomId") final int roomId, final ModelMap modelMap) {
		String view = "rooms/selectSitter";
		Collection<Sitter> sitters = this.sitterService.findAll();
		Room room = this.roomService.findRoomById(roomId);
		modelMap.addAttribute("room", room);
		modelMap.addAttribute("sitters", sitters);
		return view;
	}

	@PostMapping(value = "/rooms/{roomId}/sitter")
	public String processChangeSitter(final Room room, @PathVariable("roomId") final int roomId, final ModelMap modelMap) {
		Room room2 = this.roomService.findRoomById(roomId);
		room2.setSitter(room.getSitter());
		this.roomService.saveRoom(room2);

		return "redirect:/rooms/" + roomId;
	}

	@GetMapping(value = "/rooms/new")
	public String initCreationForm(final ModelMap modelMap) {
		String view = "rooms/createOrUpdateRoomForm";
		Room room = new Room();
		modelMap.addAttribute("room", room);
		return view;
	}

	@PostMapping(value = "/rooms/new")
	public String processSaveRoom(@Valid final Room room, final BindingResult result, final ModelMap model) {
		RoomValidator roomVal = new RoomValidator(this.roomService);
		roomVal.validate(room, result);
		if (result.hasErrors()) {
			model.addAttribute(MESSAGE, "Room not created");
			model.addAttribute("room", room);
			return "rooms/createOrUpdateRoomForm";
		} else {
			this.roomService.saveRoom(room);
			model.addAttribute(MESSAGE, "Room succesfully created");
		}
		return "redirect:/rooms/";
	}
	@GetMapping(value = "/rooms/delete/{roomId}")
	public String processDeleteRoom(@PathVariable("roomId") final int roomId, final Model model) {
		String view = "redirect:/rooms/";
		Room room = this.roomService.findRoomById(roomId);
		if (room != null && !room.getReservations().isEmpty()) {
			for (Reservation r : room.getReservations()) {
				if (!r.getStatus().getName().equals("ACCEPTED")) {
					this.roomService.delete(room);
					model.addAttribute(MESSAGE, "Event Successfuly deleted");
				}
			}
		} else if (room != null && room.getReservations().isEmpty()) {
			this.roomService.delete(room);
		} else {

			model.addAttribute("roomNotDeleted", "The room named " + room.getName() + " cannot be removed because it has reservations");
		}
		return view;
	}

	@GetMapping(value = "/rooms/{roomId}/edit")
	public String processInitUpdateForm(@PathVariable("roomId") final int roomId, final ModelMap model) {
		Room room = this.roomService.findRoomById(roomId);
		model.addAttribute(room);
		return RoomController.VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/rooms/{roomId}/edit")
	public String processUpdateRoom(@Valid final Room room, @PathVariable("roomId") final int roomId, final BindingResult result, final ModelMap modelMap) {
		RoomValidator roomVal = new RoomValidator(this.roomService, roomId);
		roomVal.validate(room, result);

		if (result.hasErrors()) {
			return RoomController.VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		} else {
			room.setId(roomId);
			this.roomService.saveRoom(room);
			return "redirect:/rooms/" + room.getId();
		}

	}
	@GetMapping("/rooms/{roomId}")
	public ModelAndView showRoom(@PathVariable("roomId") final int roomId, final ModelMap model, final HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("rooms/roomDetails");

		Room r = this.roomService.findRoomById(roomId);
		if (r.getReservations() != null ) {
			Integer numReservationsAccepted = (int) r.getReservations().stream().filter(x -> x.getStatus().getName().equals("ACCEPTED")).count();
			Boolean completedRoom = numReservationsAccepted.equals(r.getCapacity());
			model.addAttribute("completedRoom", completedRoom);
			//Para no poder eliminar una room si tiene reservas
			model.addAttribute("notHaveAcceptedReservations", numReservationsAccepted == 0);
		}
		//Para mostrar las reservas de cada usuario por cada habitacion, siempre y cuando no sea admin, ya que el admin las ve todas.
		if (!this.hasRole("admin") && !this.hasRole("sitter") && !request.getUserPrincipal().getName().equals("spring")) {
			Principal principal = request.getUserPrincipal();
			int ownerId = this.ownerService.findOwnerByUserName(principal.getName()).getId();
			Collection<Reservation> reservations = this.reservationService.findReservationsByOwnerAndRoomId(ownerId, roomId);
			if(!reservations.isEmpty()) {
			model.addAttribute("myReservations", reservations);
			}
		}
		model.put("room", this.roomService.findRoomById(roomId));
		mav.addObject(this.roomService.findRoomById(roomId));
		return mav;
	}

	protected boolean hasRole(final String role) {
		// get security context from thread local
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}

		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return false;
		}

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (role.equals(auth.getAuthority())) {
				return true;
			}
		}

		return false;
	}
}
