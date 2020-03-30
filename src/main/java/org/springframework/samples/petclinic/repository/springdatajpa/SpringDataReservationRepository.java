
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.ReservationRepository;

public interface SpringDataReservationRepository extends ReservationRepository, Repository<Reservation, Integer> {

	@Override
	@Query("SELECT r FROM Reservation r WHERE r.room.id =:roomId")
	List<Reservation> findByRoomId(@Param("roomId") int id);

	@Override
	@Query("SELECT r FROM Reservation r WHERE r.owner.id =:ownerId")
	Collection<Reservation> findByOwnerId(@Param("ownerId") int ownerId);

	@Override
	@Query("SELECT r FROM Reservation r WHERE r.id=:reservationId")
	Reservation findById(@Param(value = "reservationId") int reservationId);

	@Override
	@Query("SELECT s FROM Status s ")
	Collection<Status> findStatus();

	@Override
	@Query("SELECT s FROM Status s WHERE s.id=1")
	Status findPendingStatus();

	@Override
	@Query("SELECT s FROM Status s where s.id =:statusId ")
	Status findStatus(@Param(value = "statusId") int statusId);
}
