
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

		

		BDDMockito.given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james));
		BDDMockito.given(this.clinicService.findVetById(1)).willReturn(james);
		BDDMockito.given(this.clinicService.findSpecialiesById(VetControllerTests.TEST_SPECIALTIES_ID)).willReturn(radiology);
		BDDMockito.given(this.clinicService.findVetByUserName(VetControllerTests.TEST_USER_NAME)).willReturn(james);
		BDDMockito.given(this.clinicService.findVisits(VetControllerTests.TEST_USER_NAME)).willReturn(Lists.newArrayList(v));
		BDDMockito.given(this.clinicService.findDiagnosis(VetControllerTests.TEST_USER_NAME)).willReturn(Lists.newArrayList(d));

	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowVetListHtml() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vets")).andExpect(MockMvcResultMatchers.view().name("vets/vetList"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowVetListXml() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_XML_VALUE))
	//			.andExpect(MockMvcResultMatchers.content().node(HasXPath.hasXPath("/vets/vetList[id=1]/id")));
	//	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/create")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vet")).andExpect(MockMvcResultMatchers.view().name("vets/createVet"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessCreationFormSuccess() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/create").param("id", "1").param("firstName", "Pablo").param("lastName", "Reneses").with(SecurityMockMvcRequestPostProcessors.csrf()).param("specialties", "2"))
	//			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/admin"));
	//
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessCreationFormWithErrors() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/create").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Pablo")).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("vet")).andExpect(MockMvcResultMatchers.view().name("vets/admin"));
	//	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/mySpace"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(model().attribute("vet", hasProperty("lastName", is("Carter"))))
			.andExpect(model().attribute("vet", hasProperty("firstName", is("James"))))
			.andExpect(model().attribute("vet", hasProperty("specialties", is("1"))))
			.andExpect(MockMvcResultMatchers.view().name("vets/visitList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("vets"));
	}
	


}
