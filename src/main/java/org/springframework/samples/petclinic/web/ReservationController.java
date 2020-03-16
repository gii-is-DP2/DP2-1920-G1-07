package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.security.core.Authentication;
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


@Controller
public class ReservationController {

	private final ReservationService reservationService;

	private final PetService petService;

	private final OwnerService ownerService;

	private final RoomService roomService;
	
	@InitBinder("room")
	public void initRoomBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("reservation")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ReservationValidator());
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
		Collection<Pet> pets = petService.findPetsByOwnerName(name);
		return pets;
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
		Collection<Status> s = this.reservationService.findAllStatus();
		model.addAttribute("status", s);
		model.addAttribute("statusPending", this.reservationService.findPendingStatus());
		return "reservations/createOrUpdateReservationForm";
	}
	
//	public Boolean validaciones(Optional<Room> r,Optional<Pet> p,LocalDate d,BindingResult result) {
//		Boolean res = false;
//		Boolean diferentTypes;
//		Boolean isValidDate;
//		
//		diferentTypes = r.get().getType() != p.get().getType();
//		isValidDate = d.isBefore(LocalDate.now());
//		if(diferentTypes) {
//			FieldError errorType = new FieldError("reservation", "pet", "Esta habitacion no puede acoger mascotas de tipo "+p.get().getType().getName().toUpperCase()+", debe ser de tipo "+r.get().getType().getName().toUpperCase()+".");
//			result.addError(errorType);
//		}
//		if(isValidDate) {
//			FieldError errorType = new FieldError("reservation", "entryDate", "La fecha no puede ser superior a la fecha actual.");
//		}
//		return true;
//	}
//	
	
	@PostMapping(value = "/rooms/{roomId}/reservations/new")
	public String processNewResevationForm(@PathVariable("roomId") int roomId, @Valid Reservation reservation,
			BindingResult result, HttpServletRequest request, Model model) {
		
		model.addAttribute("statusPending", this.reservationService.findPendingStatus());
		if(reservation.getPet() == null) {
			return "reservations/createOrUpdateReservationForm";
		}else {
		if(result.hasErrors()) {
			return "reservations/createOrUpdateReservationForm";
		}
		// Si el pet no es nulo entonces realizamos lo siguiente.
		Pet p = this.petService.findPetById(Integer.parseInt(reservation.getPet()));
		Room r = this.roomService.findRoomById(roomId).get();
		if(r.getType() != p.getType()) {
			FieldError petTypeError = new FieldError("reservation","pet", "This room cannot accommodate "+p.getType().getName().toUpperCase()+"S, it can only accommodate "+r.getType().getName().toUpperCase()+"S.");
			result.addError(petTypeError);
			return "reservations/createOrUpdateReservationForm";
		}else {
			
			Owner o = this.findOwner(request);
			reservation.setStatus(this.reservationService.findPendingStatus());
			reservation.setOwner(o);
			reservation.setRoom(r);
			reservation.setPet(p.getName());
			// Restricciones
			this.reservationService.saveReservation(reservation);
			return "redirect:/rooms/{roomId}";
		}
		}
	}
	@GetMapping(value = "/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit")
	public String processInitUpdateForm(@PathVariable("roomId") int roomId, @PathVariable("ownerId") int ownerId,
			@PathVariable("reservationId") int reservationId, Model model) {
		
		Room r = this.roomService.findRoomById(roomId).get();
		Integer numReservationsAccepted = (int) r.getReservations().stream().filter(x->x.getStatus().getName().equals("ACCEPTED")).count();
		Boolean completedRoom = numReservationsAccepted == r.getCapacity();
		model.addAttribute("completedRoom", numReservationsAccepted == r.getCapacity());
		
		Reservation reservation = this.reservationService.findReservationsById(reservationId);
		Collection<Status> s = this.reservationService.findAllStatus();
		if(completedRoom) {
			Status accepted = this.reservationService.findStatusById(1);
			s.remove(accepted);
			model.addAttribute("statusWithouthAccepted", s);
		}else {
			model.addAttribute("status", s);
		}
		model.addAttribute(reservation);
		return "reservations/createOrUpdateReservationForm";
	}

	@PostMapping(value = "/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit")
	public String processUpdateRoom(@Valid Reservation reservation, @PathVariable("roomId") int roomId,@PathVariable("ownerId") int ownerId,
			@PathVariable("reservationId") int reservationId,BindingResult result, ModelMap modelMap) {
		
		if(reservation.getPet() == null) {
			return "reservations/createOrUpdateReservationForm";
		}else {
			Pet p = this.petService.findPetById(Integer.parseInt(reservation.getPet()));
			Room r = this.roomService.findRoomById(roomId).get();
			if(r.getType() != p.getType()) {
				FieldError petTypeError = new FieldError("reservation","pet", "This room cannot accommodate "+p.getType().getName().toUpperCase()+"S, it can only accommodate "+r.getType().getName().toUpperCase()+"S.");
				result.addError(petTypeError);
				return "reservations/createOrUpdateReservationForm";
		}else {
			Reservation capturedReservation = this.reservationService.findReservationsById(reservationId);
//			reservation.setId(reservationId);
			//Mantener el Room y el Owner porque sino se pierde el id
			capturedReservation.setRoom(this.roomService.findRoomById(roomId).get());
			capturedReservation.setOwner(this.ownerService.findOwnerById(ownerId));
			Status s = this.reservationService.findStatusById(reservation.getStatus().getId());
			capturedReservation.setStatus(this.reservationService.findStatusById(s.getId()));
			//Imprimir el nombre de la mascota en vez de el id
			capturedReservation.setPet(p.getName());
			
			this.reservationService.saveReservation(capturedReservation);
			return "redirect:/rooms/{roomId}";
		}
		}
		
	}
	
	public Boolean fechaValida(LocalDate date) {
		return date.isBefore(LocalDate.now());
	}

}
