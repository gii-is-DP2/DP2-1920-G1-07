
package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
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
public class VetControllerE2ETest {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_SPECIALTIES_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/create")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vet")).andExpect(MockMvcResultMatchers.view().name("vets/createVet"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/create").param("id", "1").param("firstName", "Pablo").param("lastName", "Reneses").with(SecurityMockMvcRequestPostProcessors.csrf()).param("specialties", "1"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/create").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Pablo")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("vet")).andExpect(MockMvcResultMatchers.view().name("vets/createVet"));
	}

	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/mySpace")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vet"))
			.andExpect(MockMvcResultMatchers.model().attribute("vet", Matchers.hasProperty("lastName", Matchers.is("vet2")))).andExpect(MockMvcResultMatchers.model().attribute("vet", Matchers.hasProperty("firstName", Matchers.is("Vet2"))))
			.andExpect(MockMvcResultMatchers.model().attribute("vet", Matchers.hasProperty("specialties"))).andExpect(MockMvcResultMatchers.view().name("vets/visitList"));

	}

}
