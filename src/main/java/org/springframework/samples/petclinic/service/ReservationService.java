package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

		@Autowired
		private ReservationRepository reservationRepo;
		
		@Transactional
		public Iterable<Reservation> findResrvationsByRoomId(int roomId) throws DataAccessException{
			return reservationRepo.findByRoomId(roomId);
		}
		
		@Transactional
		public void saveReservation(@Valid Reservation reservation) throws DataAccessException{
			reservationRepo.save(reservation);
		}
		@Transactional
		public void deleteReservation(@Valid Reservation reservation) throws DataAccessException {
			reservationRepo.delete(reservation);
		}
		@Transactional
		public Reservation findReservationsById(int reservationId) {
			return reservationRepo.findById(reservationId);
		}
		@Transactional
		public Collection<Status> findAllStatus(){
			return reservationRepo.findStatus();
		}
		@Transactional
		public Status findPendingStatus() {
			return reservationRepo.findPendingStatus();
		}
		
		@Transactional
		public Status findStatusById(int id) {
			return reservationRepo.findStatus(id);
		}

		public Collection<Reservation> findReservationsByOwnerAndRoomId(int ownerId, int roomId) {
			return reservationRepo.findByOwnerId(ownerId,roomId);
		}
		
}
