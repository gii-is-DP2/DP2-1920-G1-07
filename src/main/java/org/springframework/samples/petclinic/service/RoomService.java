package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepo;
	
	@Transactional
	public int roomCount() { 
		return (int) roomRepo.count();
	}
	
	@Transactional
	public Iterable<Room> allRooms() {
		return roomRepo.findAll();
	}
}
