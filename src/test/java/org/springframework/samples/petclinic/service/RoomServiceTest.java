package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Provider.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RoomServiceTest {
	
	@Autowired
	private RoomService roomService;
	
//	@Test
//	public void testCountRoomWithInitialData() {
//		int count = roomService.roomCount();
//		assertEquals(count, 1);
//	}
}
