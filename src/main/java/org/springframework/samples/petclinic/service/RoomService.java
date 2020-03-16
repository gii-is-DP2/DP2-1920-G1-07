package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

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
	public Iterable<Room> allRooms() throws DataAccessException{
		return roomRepo.findAllRooms();
	}
	@Transactional
	public void saveRoom(@Valid Room room) throws DataAccessException {
		roomRepo.save(room);
		
	}

	@Transactional
	public void delete(@Valid Room room) throws DataAccessException{
		roomRepo.delete(room);
	}
	
	@Transactional
	public Optional<Room> findRoomById(int roomId) throws DataAccessException{
		return roomRepo.findById(roomId);
	}
}
