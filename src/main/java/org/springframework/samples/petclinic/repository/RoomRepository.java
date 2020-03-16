package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;

import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository {

	Collection<Room> findAllRooms() throws DataAccessException;
	
	void save(Room room) throws DataAccessException;
	
	void delete(Room room) throws DataAccessException;
	
	Optional<Room> findById(int roomId) throws DataAccessException;
	
	
	
}
