package org.springframework.samples.petclinic.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class DonationServiceTests {
	
	@Autowired
	private DonationService donationService;
	
	@Test
	public void testMyDonations() {
				
		int count = donationService.findMyDonations("admin1").size();
		
		
		assertEquals(count,3);
		assertNotEquals(count, 0);
	}
	
	

	@Test
	public void testMyDonationsCause() {
				
		int count = donationService.findDonationCause(3).size();
		
		
		assertEquals(count,3);
		assertNotEquals(count, 4);
	}
	

	
	

}
