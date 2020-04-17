
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepo;


	//	@Transactional
	//	public int roomCount() throws DataAccessException{ 
	//			return (int) roomRepo.count();
	//	}
	//	

	@Transactional
	public Collection<Room> allRooms() throws DataAccessException {
		return this.roomRepo.findAll();
	}
	@Transactional
	public void saveRoom(@Valid final Room room) throws DataAccessException {
		this.roomRepo.save(room);

	}

	@Transactional
	public void delete(@Valid final Room room) throws DataAccessException {
		this.roomRepo.delete(room);
	}

	@Transactional
	public Room findRoomById(final int roomId) throws DataAccessException {
		return this.roomRepo.findById(roomId);
	}
	public Iterable<Room> findRoomsBySitterUserName(final String name) {
		return this.roomRepo.findRoomsBySitterUserName(name);
	}
}
