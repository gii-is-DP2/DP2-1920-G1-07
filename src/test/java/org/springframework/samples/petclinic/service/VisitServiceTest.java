package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))


public class VisitServiceTest {

	
	@Autowired
	private VisitService visitService;
	
	@Test
	public void testMyVisits() {
	
		Boolean visits = visitService.visitFind().toString().contains("Visit");
		
		assertEquals(visits,true);
		assertNotEquals(visits, false);
	}
}
