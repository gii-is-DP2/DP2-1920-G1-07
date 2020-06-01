package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.web.RequestControllerTests;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
public class RequestControllerE2ETest {

	private static final int	TEST_OWNER_ID	= 10;
	private static final int	TEST_REQUEST_ID	= 2;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/request/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/requestForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("request"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		mockMvc.perform(post("/request/new")
			.with(csrf())
			.param("address", "Calle k"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("request"))
		.andExpect(view().name("request/requestForm"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/request/new")
			.with(csrf())
			.param("address", "Calle k")
			.param("telephone", "666666666")
			.param("type", "hamster"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testDuplicatedProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/request/new")
			.with(csrf())
			.param("address", "Calle k")
			.param("telephone", "666666666")
			.param("type", "hamster"))
		.andExpect(status().isOk())
		.andExpect(view().name("request/requestForm"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("request/requestList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("requests"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testShowOnlyAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testReject() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/reject", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testAccept() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/accept", TEST_REQUEST_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}
}
