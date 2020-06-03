package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;


@WebMvcTest(controllers=DonationController.class,
		excludeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration = SecurityConfiguration.class)
 class DonationControllerTest {
	
	public static final int TEST_DONATION_ID = 1;
	public static final int TEST_CAUSE_ID = 1;
	
	@MockBean
	private DonationService donationService;
	
	@MockBean
	private CauseService causeService;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Donation donation;
	
	private Cause cause;
	
	
	
	
	
	@BeforeEach
	void setup() {
		
		cause = new Cause();
		cause.setId(DonationControllerTest.TEST_CAUSE_ID);
		cause.setTitle("Test cause");
		cause.setDescription("This is a test cause");
		cause.setDeadline(LocalDate.of(2020, 10, 15));
		cause.setMoney(10000.0);
		
		Status status = new Status();
		status.setName("PENDING");
		
		User user = this.userService.findUserByUserName("owner1");
		
		cause.setStatus(status);
		cause.setUser(user);
		
		donation = new Donation();
		donation.setAnonymous(false);
		donation.setCauses(cause);
		donation.setId(DonationControllerTest.TEST_DONATION_ID);
		donation.setMoney(5000.0);
		donation.setMoneyRest(5000.0);
		donation.setUser(user);
		
		BDDMockito.given(this.causeService.findCauseById(DonationControllerTest.TEST_CAUSE_ID)).willReturn(this.cause);
		
		BDDMockito.given(this.donationService.findDonationByIdNotOptional(DonationControllerTest.TEST_DONATION_ID))
		.willReturn(this.donation);
		
	}
	
	@WithMockUser(value="spring")
	@Test
	void testListDonations() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/cause/{causeId}/donations", TEST_CAUSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	@WithMockUser(value="spring")
	@Test
	void testInitCreationForm() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/cause/{causeId}/donations/new",TEST_CAUSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("donation"))
		.andExpect(MockMvcResultMatchers.view().name("donations/editDonation"));
	}
	

	@WithMockUser(value="spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/cause/{causeId}/donations/new",TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("anonymous", "true")
				.param("money", "5000.0"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
		
	}
	
	@WithMockUser(value="spring")
	@Test
	void testProcessCreationFormHasErrorsOnMoney() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/cause/{causeId}/donations/new",TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("anonymous", "true")
				.param("money", "0.0"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("donation"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("donation", "money"))
				.andExpect(MockMvcResultMatchers.view().name("donations/editDonation"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testProcessCreationFormHasErrorsOnAnonymous() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/cause/{causeId}/donations/new",TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("anonymous", "12312979")
				.param("money", "0.5"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("donation"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("donation", "anonymous"))
				.andExpect(MockMvcResultMatchers.view().name("donations/editDonation"));
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

