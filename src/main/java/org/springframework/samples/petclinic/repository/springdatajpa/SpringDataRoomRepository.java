
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
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
	Room findById(@Param("roomId") int roomId);

	@Override
	@Query("SELECT r from Room r where r.sitter.user.username = :name")
	Iterable<Room> findRoomsBySitterUserName(@Param("name") String name);

	@Query("SELECT r.name from Room r")
	Collection<String> findAllRoomsNames();
}
