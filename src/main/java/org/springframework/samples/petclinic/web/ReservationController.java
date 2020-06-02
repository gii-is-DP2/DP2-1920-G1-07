package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
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

@Controller
public class ReservationController {
	
	private static final String STATUS = "status";
	private static final String CREATERESERVATIONFORM = "reservations/createReservationForm";
	private static final String REDIRECT_ROOMS = "redirect:/rooms/{roomId}";
	private final ReservationService reservationService;
	
	@Autowired
	private final PetService petService;

	private final OwnerService ownerService;

	private final RoomService roomService;
	
	private ReservationValidator resValidator;
	
	@InitBinder("room")
	public void initRoomBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("reservation")
	public void initReservationBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(resValidator);
	}

	@Autowired
	public ReservationController(ReservationService reservationService, OwnerService ownerService,
			PetService petService, RoomService roomService) {
		this.reservationService = reservationService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.roomService = roomService;
	}

	@ModelAttribute("petsOfOwner")
	public Collection<Pet> findAllPetsByOwnerId(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String name = principal.getName();
		return petService.findPetsByOwnerName(name);
	}

	@ModelAttribute("owner")
	public Owner findOwner(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String name = principal.getName();
		return this.ownerService.findOwnerByUserName(name);
	}

	@GetMapping(value = "/rooms/{roomId}/reservations/new")
	public String initNewReservationForm(@PathVariable("roomId") int roomId, Model model) {
		Reservation reservation = new Reservation();
		model.addAttribute("reservation", reservation);
		model.addAttribute("roomId", roomId);
		Collection<Status> s = this.reservationService.findAllStatus();
		model.addAttribute(STATUS, s);
		model.addAttribute("statusPending", this.reservationService.findPendingStatus());
		return CREATERESERVATIONFORM;
	}
	
	@PostMapping(value = "/rooms/{roomId}/reservations/new")
	public String processNewResevationForm(@PathVariable("roomId") int roomId, @Valid Reservation reservation,
			BindingResult result, HttpServletRequest request, Model model) {
		model.addAttribute("statusPending", this.reservationService.findPendingStatus());
		if(reservation.getPet() == null) {
			result.rejectValue("pet", "This field is required");
			return CREATERESERVATIONFORM;
		}else {
			int petId = Integer.parseInt(reservation.getPet()); 
			Owner o = this.findOwner(request);
			Pet p = this.petService.findPetById(petId);
			Room r = this.roomService.findRoomById(roomId);
			reservation.setRoom(r);
			reservation.setOwner(o);
			Status pending = this.reservationService.findPendingStatus();
			reservation.setStatus(pending);
			ReservationValidator reservationValidator = new ReservationValidator(this.petService);
			reservationValidator.validate(reservation, result);
			if(result.hasErrors()) {
				return CREATERESERVATIONFORM;
			}else {
				reservation.setPet(p.getName());
			// Restricciones
				this.reservationService.saveReservation(reservation);
				return REDIRECT_ROOMS;
			}
		}
	}
	@GetMapping(value = "/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit")
	public String processInitUpdateForm(@PathVariable("roomId") int roomId, @PathVariable("ownerId") int ownerId,
			@PathVariable("reservationId") int reservationId, Model model) {
		Boolean completedRoom = false;
		Room r = this.roomService.findRoomById(roomId);
		if(r.getReservations()!= null) {
		Integer numReservationsAccepted = (int) r.getReservations().stream().filter(x->x.getStatus().getName().equals("ACCEPTED")).count();
		completedRoom = numReservationsAccepted.equals(r.getCapacity());
		model.addAttribute("completedRoom", numReservationsAccepted.equals(r.getCapacity()));
		}
		Reservation reservation = this.reservationService.findReservationsById(reservationId);
		Collection<Status> s = this.reservationService.findAllStatus();
		if(completedRoom.equals(true)) {
			Status accepted = this.reservationService.findStatusById(2); 
			s.remove(accepted);
			model.addAttribute("statusWithouthAccepted", s);
		}else {
			model.addAttribute(STATUS, s);
		}
		model.addAttribute("editreservation",reservation);
		return "reservations/updateReservationForm";
	}

	@PostMapping(value = "/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit")
	public String processUpdateReservationForm(@Valid Reservation reservation, @PathVariable("roomId") int roomId,@PathVariable("ownerId") int ownerId,
			@PathVariable("reservationId") int reservationId,BindingResult result, ModelMap modelMap) {
		Reservation captReservation = this.reservationService.findReservationsById(reservationId);
		Pet p = this.petService.findPetById(Integer.parseInt(reservation.getPet()));
		reservation.setId(reservationId);
		reservation.setRoom(this.roomService.findRoomById(roomId));
		reservation.setOwner(this.ownerService.findOwnerById(ownerId));
		ReservationValidator reservaValidator = new ReservationValidator(this.petService);
		reservaValidator.validate(reservation, result);
		if(result.hasErrors()) {
			modelMap.addAttribute("editreservation", captReservation);
			modelMap.addAttribute("completedRoom", false);
			modelMap.addAttribute(STATUS, this.reservationService.findAllStatus());
			return "reservations/updateReservationForm";
		}else {
			captReservation.setRoom(this.roomService.findRoomById(roomId));
			captReservation.setOwner(this.ownerService.findOwnerById(ownerId));
			captReservation.setPet(p.getName());
			reservation.setPet(p.getName());

			this.reservationService.saveReservation(reservation);
			return REDIRECT_ROOMS;
		}
	}
	@GetMapping(value = "/rooms/{roomId}/reservation/{reservationId}/delete")
	public String processDeleteReservation(@PathVariable("roomId") int roomId, @PathVariable("reservationId") int reservationId, ModelMap model) {
		Reservation reservation = this.reservationService.findReservationsById(reservationId);
		this.reservationService.deleteReservation(reservation);
		return REDIRECT_ROOMS;
	}

}
