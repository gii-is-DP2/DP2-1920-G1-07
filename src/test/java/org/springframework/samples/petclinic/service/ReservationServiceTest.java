package org.springframework.samples.petclinic.service;



import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservationServiceTest {
	
	@Autowired
	protected ReservationService reservationService;
	
	@Autowired
	protected RoomService roomService;
	
	@Autowired
	protected OwnerService ownerService;
	
	@Test
	void shouldFindReservationByRoomId() {
		/*
		 * El owner 11 tiene una reserva en la room 1 y la 3
		 */
		Collection<Reservation> reservations = this.reservationService.findReservationsByOwnerAndRoomId(11,1);
		assertThat(reservations.size()).isEqualTo(1);
		
		/*
		 * El owner 12 tiene una reserva en la room 2 y la 4
		 */
		reservations = this.reservationService.findReservationsByOwnerAndRoomId(12, 2);
		assertThat(reservations.size()).isEqualTo(1);
		/*
		 * El owner 11 NO tiene una reserva en la room 2
		 * por lo tanto la busqueda debe devolver un conjunto vacío.
		 */
		reservations = this.reservationService.findReservationsByOwnerAndRoomId(11, 2);
		assertThat(reservations.isEmpty()).isTrue();
	}
	
	@Test
	void shouldInsertReservationToARoom() {
		Room room = this.roomService.findRoomById(1);
		Collection<Reservation> reservations = this.reservationService.findReservationsByOwnerAndRoomId(11, 1);
		Owner owner = this.ownerService.findOwnerById(11);
		int totalReservationsRoom1 = reservations.size();
		Status status = this.reservationService.findPendingStatus();
		
		
		Reservation newReservation = new Reservation();
		newReservation.setId(5);
		newReservation.setEntryDate(LocalDate.of(2020, 06, 10));
		newReservation.setExitDate(LocalDate.of(2020, 06, 15));
		newReservation.setPet(owner.getPet("Pet Snake").getId().toString());
		newReservation.setRoom(room);
		newReservation.setStatus(status);
		newReservation.setOwner(owner);
		
		this.reservationService.saveReservation(newReservation);
		assertThat(newReservation.getId().longValue()).isNotEqualTo(0);
		
		reservations = this.reservationService.findReservationsByOwnerAndRoomId(11, 1);
		assertThat(reservations.size()).isEqualTo(totalReservationsRoom1 + 1);
	}
	@Test
	void shouldDeleteARoomReservation() {
		Room room = this.roomService.findRoomById(1);
		Collection<Reservation> reservations = room.getReservations(); 
		Reservation reservation = this.reservationService.findReservationsById(1);
		int totalReservations = reservations.size();
		
		this.reservationService.deleteReservation(reservation);
		reservations.remove(reservation);
		
		room = this.roomService.findRoomById(1);
		reservations = room.getReservations();
		assertThat(reservations.size()).isEqualTo(totalReservations - 1);
	}
	
	@Test
	void shouldUpdateARoomReservation() {
		Room room = this.roomService.findRoomById(1);
		Reservation reservation = room.getReservations().stream().collect(Collectors.toList()).get(0);
		//Le añado 5 dias a la fecha de entrada
		LocalDate newDate = reservation.getEntryDate().plusDays(5);
		reservation.setEntryDate(newDate);
		this.reservationService.saveReservation(reservation);
		reservation = room.getReservations().stream().collect(Collectors.toList()).get(0);
		assertThat(reservation.getEntryDate()).isEqualTo(newDate);
	}
	
	@Test
	void shouldFindCorrectReservationById() {
		Reservation reservation = this.reservationService.findReservationsById(1);
		assertThat(reservation.getRoom().getId().longValue()).isEqualTo(1);
		
		reservation = this.reservationService.findReservationsById(1);
		assertThat(reservation.getRoom().getId().longValue()).isNotEqualTo(2);
	}
	
	@Test
	void shouldFindOnlyPendingStatus() {
		Status pendingStatus = this.reservationService.findPendingStatus();
		assertThat(pendingStatus.getName()).isEqualTo("PENDING");
	}
	@Test
	void shouldFindCorrectStatusById() {
		Status status = this.reservationService.findStatusById(1);
		assertThat(status.getName()).isEqualTo("PENDING");
		
		status = this.reservationService.findStatusById(2);
		assertThat(status.getName()).isEqualTo("ACCEPTED");
		
		status = this.reservationService.findStatusById(3);
		assertThat(status.getName()).isEqualTo("REJECTED");
		
	}
}