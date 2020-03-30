package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;

import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository {

	void save(Room room) throws DataAccessException;
	
	void delete(Room room) throws DataAccessException;
	
	Room findById(int roomId) throws DataAccessException;
	
	Collection<Room> findAll() throws DataAccessException;
	
	
}
