package org.springframework.samples.petclinic.service;


import static org.assertj.core.api.Assertions.assertThat;


import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoomServiceTest {
	
	@Autowired
	private RoomService roomService;
	
	@Test
	void shoudFindRoomByCorrectId() {
		Room room  = this.roomService.findRoomById(1);
		assertThat(room.getName()).isEqualTo("Room1");
		
	}
	@Test
	void shouldSaveRoom() {
		Collection<Room> rooms = this.roomService.allRooms();
		int totalRooms = rooms.size();
		
		PetType p = new PetType();
		p.setId(1);
		p.setName("dog");
		
		Room newRoom = new Room();
		newRoom.setId(5); //Se le asigna un nuevo Id libre.
		newRoom.setName("Room1");
		newRoom.setCapacity(4);
		newRoom.setType(p);
		
		this.roomService.saveRoom(newRoom);
		assertThat(newRoom.getId().longValue()).isNotEqualTo(0);
		
		rooms = this.roomService.allRooms();
		assertThat(rooms.size()).isEqualTo(totalRooms + 1);
		
	}
	
	@Test
	void shouldUpdateRoom() {
		Room room = this.roomService.findRoomById(1);
		String oldName = room.getName();
		String newName = (oldName + " Update");
		
		room.setName(newName);
		this.roomService.saveRoom(room);
		
		room = this.roomService.findRoomById(1);
		assertThat(room.getName()).isEqualTo(newName);
	}
	
	@Test
	void shouldDeleteRoom() {
		Collection<Room> rooms = this.roomService.allRooms();
		int totalRooms = rooms.size();
		
		Room room = this.roomService.findRoomById(1);
		
		this.roomService.delete(room);
		
		rooms = this.roomService.allRooms();
		assertThat(rooms.size()).isEqualTo(totalRooms - 1);
	}
}
