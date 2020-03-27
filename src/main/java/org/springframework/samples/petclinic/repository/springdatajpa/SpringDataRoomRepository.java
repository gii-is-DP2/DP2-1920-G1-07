
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.repository.RoomRepository;

public interface SpringDataRoomRepository extends RoomRepository, Repository<Room, Integer> {

	@Override
	@Query("SELECT room FROM Room room")
	Collection<Room> findAllRooms();

	@Override
	@Query("SELECT r from Room r where r.id =:roomId")
	Optional<Room> findById(@Param("roomId") int roomId);

}
