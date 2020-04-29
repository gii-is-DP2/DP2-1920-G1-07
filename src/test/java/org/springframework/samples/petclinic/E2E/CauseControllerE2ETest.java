
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CauseControllerE2ETest {

	private static final int	TEST_CAUSE_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "10000.0").param("deadline", "2020/07/25")
			.param("status.name", "PENDING")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/cause"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormHasErrorsOnDate() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "10000.0").param("deadline", "2019/07/25"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "deadline"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormHasErrorsOnMoney() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "-10000.0").param("deadline", "2019/07/25"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "money"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testInitFindFormCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause")).andExpect(MockMvcResultMatchers.view().name("causes/causesList"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testFormMyCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/myCauses/owner1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("causes/myCausesList"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testFormPendingCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/PendingCauses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/pendingCauses"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateStatusForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/PendingCauses/cause/{causeId}/edit", CauseControllerE2ETest.TEST_CAUSE_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/updatePendingCauseForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/PendingCauses/cause/{causeId}/edit", CauseControllerE2ETest.TEST_CAUSE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("status.name", "ACCEPTED"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

}
