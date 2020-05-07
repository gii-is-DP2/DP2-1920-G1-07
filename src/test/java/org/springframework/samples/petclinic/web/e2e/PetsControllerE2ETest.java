package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@TestPropertySource(
//	locations = "classpath:application-mysql.properties")
public class PetsControllerE2ETest {


	private static final int TEST_OWNER_ID = 10;
	private static final int TEST_PET_ID = 13;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owner/pets/new")).andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testOnlyOwnerCanCreatePet() throws Exception {
		mockMvc.perform(get("/owner/pets/new")).andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owner/pets/new")
			.with(csrf())
			.param("name", "xWFASD")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owner/pets"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owner/pets/new")
			.with(csrf())
			.param("birthDate", "2015/02/12"))
		.andExpect(model().attributeHasErrors("pet"))
		.andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessCreationFormDateFuture() throws Exception {
		mockMvc.perform(post("/owner/pets/new")
			.with(csrf())
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2021/02/12"))
		.andExpect(model().attributeHasErrors("pet"))
		.andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owner/pets/{petId}/edit", TEST_PET_ID))
		.andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/owner/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owner/pets"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owner/pets/{petId}/edit", TEST_PET_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("birthDate", "2015/02/12"))
		.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
}
