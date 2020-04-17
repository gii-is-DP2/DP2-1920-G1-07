
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository {

	void save(Room room) throws DataAccessException;

	void delete(Room room) throws DataAccessException;

	Room findById(int roomId) throws DataAccessException;

	Collection<Room> findAll() throws DataAccessException;

	Collection<Room> findAllRooms() throws DataAccessException;

	Iterable<Room> findRoomsBySitterUserName(String name) throws DataAccessException;

}
