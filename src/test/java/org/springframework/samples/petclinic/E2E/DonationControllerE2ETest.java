package org.springframework.samples.petclinic.E2E;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class DonationControllerE2ETest {
	

	public static final int TEST_DONATION_ID = 1;
	public static final int TEST_CAUSE_ID = 1;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(username="admin1",authorities = {
			"admin"
	})
	@Test
	void testListDonations() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/cause/{causeId}/donations", TEST_CAUSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	@WithMockUser(username="admin1",authorities = {
			"admin"
	})
	@Test
	void testInitCreationForm() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/cause/{causeId}/donations/new",TEST_CAUSE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("donation"))
		.andExpect(MockMvcResultMatchers.view().name("donations/editDonation"));
	}
	

	@WithMockUser(username="admin1",authorities = {
			"admin"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/cause/{causeId}/donations/new",TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("anonymous", "true")
				.param("money", "5000.0"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
		
	}
	
	@WithMockUser(username="admin1",authorities = {
			"admin"
	})
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
	
	@WithMockUser(username="admin1",authorities = {
			"admin"
	})
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
