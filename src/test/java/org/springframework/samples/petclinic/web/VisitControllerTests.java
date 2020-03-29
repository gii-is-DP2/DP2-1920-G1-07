package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_PET_ID = 1;
	private static final int TEST_VISIT_ID = 1;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_DIAGNOSES_ID = 1;
	private static final int TEST_PETTYPE_ID = 1;

//	@Autowired
//	private VisitController visitController;

	@MockBean
	private PetService clinicService;

	@MockBean
	private VisitService visitService;

	@MockBean
	private UserService userService;

	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;

	private Visit visit;

	private Vet vet;

	private Diagnosis diagnoses;

	private Pet pet;

	private PetType petType;

	@BeforeEach
	void setup() {

		User user = this.userService.findUserByUserName("owner");

		vet = new Vet();
		vet.setId(VisitControllerTests.TEST_VET_ID);
		vet.setFirstName("VetTest");
		vet.setLastName("VetTestTwo");
		vet.setUser(user);
		Set<Diagnosis> d = new HashSet<>();
		Set<Visit> v = new HashSet<>();
		vet.setDiagnoses(d);
		vet.setVisits(v);

		petType = new PetType();
		petType.setId(VisitControllerTests.TEST_PETTYPE_ID);
		petType.setName("dog");

		pet = new Pet();
		pet.setBirthDate(LocalDate.of(2019, 12, 23));
		pet.setDiagnoses(d);
		pet.setId(VisitControllerTests.TEST_PET_ID);
		pet.setName("TestName");
		pet.setType(petType);

		diagnoses = new Diagnosis();
		diagnoses.setDate(LocalDate.of(2020, 9, 24));
		diagnoses.setDescription("Teest");
		diagnoses.setId(VisitControllerTests.TEST_DIAGNOSES_ID);
		diagnoses.setPet(pet);
		diagnoses.setVet(vet);
		diagnoses.setVisit(visit);

		visit = new Visit();
		visit.setDate(LocalDate.of(2020, 10, 24));
		visit.setDescription("Test description");
		visit.setDiagnosis(diagnoses);
		visit.setId(VisitControllerTests.TEST_VISIT_ID);
		visit.setPet(pet);
		visit.setVet(vet);

		BDDMockito.given(this.clinicService.findPetById(VisitControllerTests.TEST_PET_ID)).willReturn(this.pet);
		BDDMockito.given(this.visitService.findVisitById(VisitControllerTests.TEST_VISIT_ID)).willReturn(this.visit);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owner/pets/{petId}/visits/new?vetId={vetId}", TEST_PET_ID,TEST_VET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visit"))
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/owner/pets/{petId}/visits/new?vetId={vetId}", TEST_PET_ID,TEST_VET_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "Visit Description"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
			
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/owner/pets/{petId}/visits/new?vetId={vetId}", TEST_PET_ID,TEST_VET_ID, TEST_PET_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visit"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	}
}
