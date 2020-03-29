
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_SPECIALTIES_ID	= 1;

	private static final String	TEST_USER_NAME		= "spring";

	@Autowired
	private VetController		vetController;

	@MockBean
	private VetService			clinicService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		User user = new User();
		user.setUsername("spring");
		user.setPassword("password");
		user.setEnabled(true);
	
		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		james.setUser(user);
		
		Visit v = new Visit();
		v.setId(1);

		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		james.addSpecialty(radiology);
		
		Diagnosis d = new Diagnosis();
		d.setId(1);
		d.setDescription("bien");
		d.setDate(LocalDate.now());

		

		BDDMockito.given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james));
		BDDMockito.given(this.clinicService.findVetById(1)).willReturn(james);
		BDDMockito.given(this.clinicService.findSpecialiesById(VetControllerTests.TEST_SPECIALTIES_ID)).willReturn(radiology);
		BDDMockito.given(this.clinicService.findVetByUserName(VetControllerTests.TEST_USER_NAME)).willReturn(james);
		BDDMockito.given(this.clinicService.findVisits(VetControllerTests.TEST_USER_NAME)).willReturn(Lists.newArrayList(v));
		BDDMockito.given(this.clinicService.findDiagnosis(VetControllerTests.TEST_USER_NAME)).willReturn(Lists.newArrayList(d));

	}


	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(get("/vets/create"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("vet"))
		.andExpect(view().name("vets/createVet"));
	}

		@WithMockUser(value = "spring")
		@Test
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc.perform(post("/vets/create")
					.param("id", "1")
					.param("firstName", "Pablo")
					.param("lastName", "Reneses")
					.with(csrf())
					.param("specialties", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/admin"));
	
		}
	
		@WithMockUser(value = "spring")
		@Test
		void testProcessCreationFormWithErrors() throws Exception {
			this.mockMvc.perform(post("/vets/create").with(csrf())
				.param("firstName", "Pablo"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(view().name("vets/createVet"));
		}	
	
	@WithMockUser(value = "spring")
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(get("/vets/mySpace"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("vet"))
			.andExpect(model().attribute("vet", hasProperty("lastName", is("Carter"))))
			.andExpect(model().attribute("vet", hasProperty("firstName", is("James"))))
			.andExpect(model().attribute("vet", hasProperty("specialties")))
			.andExpect(view().name("vets/visitList"));
	}
	


}
