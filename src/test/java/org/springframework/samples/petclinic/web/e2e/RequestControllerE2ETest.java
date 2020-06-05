
package org.springframework.samples.petclinic.web.e2e;

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
//@TestPropertySource(locations = "classpath:application-mysql.properties")
class RequestControllerE2ETest {

	private static final int	TEST_OWNER_ID	= 10;
	private static final int	TEST_REQUEST_ID	= 2;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/request/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/requestForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("request"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/request/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "Calle k")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("request")).andExpect(MockMvcResultMatchers.view().name("request/requestForm"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/request/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "Calle k").param("telephone", "666666666").param("type", "hamster"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDuplicatedProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/request/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "Calle k").param("telephone", "666666666").param("type", "hamster"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/requestForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/requestList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("requests"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testShowOnlyAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testReject() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/reject", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testAccept() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/accept", RequestControllerE2ETest.TEST_REQUEST_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}
}
