
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class DonationServiceTests {

	@Autowired
	private DonationService donationService;


	@Test
	void testMyDonations() {

		int count = this.donationService.findMyDonations("admin1").size();

		Assertions.assertEquals(3, count);
		Assertions.assertNotEquals(0, count);
	}

	@Test
	void testMyDonationsCause() {

		int count = this.donationService.findDonationCause(3).size();

		Assertions.assertEquals(3, count);
		Assertions.assertNotEquals(4, count);
	}

}
