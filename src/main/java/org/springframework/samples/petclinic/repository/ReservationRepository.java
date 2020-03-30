
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Status;

public interface ReservationRepository {

	void save(Reservation reservation) throws DataAccessException;

	List<Reservation> findByRoomId(int ownerId);

	void delete(@Valid Reservation reservation) throws DataAccessException;

	Reservation findById(int reservationId) throws DataAccessException;

	Collection<Status> findStatus() throws DataAccessException;

	Status findPendingStatus() throws DataAccessException;

	Status findStatus(int statusId) throws DataAccessException;

	Collection<Reservation> findByOwnerId(int ownerId, int roomId) throws DataAccessException;

}
