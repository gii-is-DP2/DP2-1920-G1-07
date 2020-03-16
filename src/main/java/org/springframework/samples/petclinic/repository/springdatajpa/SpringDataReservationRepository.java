package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataReservationRepository extends ReservationRepository,CrudRepository<Reservation, Integer>{
	
	@Override
	@Query("SELECT r FROM Reservation r WHERE r.room.id =:roomId")
	public List<Reservation> findByRoomId(@Param("roomId") int id);

	@Override
	@Query("SELECT r FROM Reservation r WHERE r.owner.id =:ownerId")
	public Collection<Reservation> findByOwnerId(@Param("ownerId")int ownerId);
	
	@Override
	@Query("SELECT r FROM Reservation r WHERE r.id=:reservationId")
	Reservation findById(@Param(value = "reservationId") int reservationId);
	
	@Override
	@Query("SELECT s FROM Status s ")
	Collection<Status> findStatus();
	
	@Query("SELECT s FROM Status s WHERE s.id=2")
	Status findPendingStatus();
	
	@Override
	@Query("SELECT s FROM Status s where s.id =:statusId ")
	Status findStatus(@Param(value = "statusId")int statusId);
}
