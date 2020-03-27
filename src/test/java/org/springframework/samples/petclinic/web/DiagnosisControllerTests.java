
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = DiagnosisController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class DiagnosisControllerTests {

	private static final int	TEST_VET_ID			= 1;

//	private static final int	TEST_SPECIALTIES_ID	= 1;
//
//	private static final String	TEST_USER_NAME		= "spring";

	@Autowired
	public DiagnosisController	diagnosisController;

	@MockBean
	public DiagnosisService	clinicService;

	@MockBean
	public VetService			vetService;
	


	@Autowired
	public MockMvc				mockMvc;


	@BeforeEach
	void setup() {
//		Vet james = new Vet();
//		james.setFirstName("James");
//		james.setLastName("Carter");
//		james.setId(1);
//		
//		Vet helen = new Vet();
//		helen.setFirstName("Helen");
//		helen.setLastName("Leary");
//		helen.setId(2);
//		
//		Specialty radiology = new Specialty();
//		radiology.setId(1);
//		radiology.setName("radiology");
//		helen.addSpecialty(radiology);
		

	}


	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vet/{vetId}/diagnosis", TEST_VET_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("diagnosis"))
		.andExpect(MockMvcResultMatchers.view().name("vets/createDiagnosis"));
	}

	

}
