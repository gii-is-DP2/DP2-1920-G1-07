
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitControllerTests {

	private static final int	TEST_PET_ID	= 1;

	@Autowired
	private VisitController		visitController;

	@MockBean
	private PetService			clinicService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		BDDMockito.given(this.clinicService.findPetById(VisitControllerTests.TEST_PET_ID)).willReturn(new Pet());
	}

	//        @WithMockUser(value = "spring")
	//        @Test
	//	void testInitNewVisitForm() throws Exception {
	//		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testProcessNewVisitFormSuccess() throws Exception {
	//		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID).param("name", "George")
	//							.with(csrf())
	//							.param("description", "Visit Description"))                                
	//                .andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testProcessNewVisitFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "George"))
	//				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testShowVisits() throws Exception {
	//		mockMvc.perform(get("/owners/*/pets/{petId}/visits", TEST_PET_ID)).andExpect(status().isOk())
	//				.andExpect(model().attributeExists("visits")).andExpect(view().name("visitList"));
	//	}

}
