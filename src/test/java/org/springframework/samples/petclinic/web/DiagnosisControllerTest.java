
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = DiagnosisController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class DiagnosisControllerTests {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_DIAGNOSIS_ID	= 1;

	private static final int	TEST_VISIT_ID		= 1;

	private static final int	TEST_PET_ID			= 1;

	@Autowired
	public DiagnosisController	diagnosisController;

	@MockBean
	public DiagnosisService		clinicService;

	@MockBean
	public VetService			vetService;

	@MockBean
	public VisitService			visitService;

	@Autowired
	public MockMvc				mockMvc;

	private Diagnosis			d;


	@BeforeEach
	void setup() {
		this.d = new Diagnosis();
		this.d.setId(DiagnosisControllerTests.TEST_DIAGNOSIS_ID);
		this.d.setDescription("Prueba");
		this.d.setDate(LocalDate.of(2020, 3, 15));

		Visit v = new Visit();
		v.setDescription("Prueba");
		v.setDate(LocalDate.of(2020, 1, 15));
		v.setId(DiagnosisControllerTests.TEST_VISIT_ID);

		Pet p = new Pet();
		p.setId(DiagnosisControllerTests.TEST_PET_ID);
		p.setName("Roc");
		v.setPet(p);
		this.d.setVisit(v);

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(DiagnosisControllerTests.TEST_VET_ID);
		this.d.setVet(james);
		this.d.setPet(v.getPet());

		BDDMockito.given(this.vetService.findVetById(DiagnosisControllerTests.TEST_VET_ID)).willReturn(james);
		BDDMockito.given(this.visitService.findById(DiagnosisControllerTests.TEST_VISIT_ID)).willReturn(v);
		BDDMockito.given(this.clinicService.findMyDiagnosis(DiagnosisControllerTests.TEST_PET_ID)).willReturn(Lists.newArrayList(this.d));

	}

	//test para que te lleve al formulario de creación de diagnositico
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vet/{vetId}/diagnosis", DiagnosisControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("diagnosis"))
			.andExpect(MockMvcResultMatchers.view().name("vets/createDiagnosis"));
	}

	//test para comprobar la creacion de un diagnostico correctamente
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vet/{vetId}/diagnosis", DiagnosisControllerTests.TEST_VET_ID).param("description", "Prueba").param("date", "2020/03/15").with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/mySpace/"));
	}

	//test de creacion de un diagnostico con fallo, ya que faltaria poner date
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vet/{vetId}/diagnosis", DiagnosisControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Prueba")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("diagnosis")).andExpect(MockMvcResultMatchers.view().name("vets/createDiagnosis"));
	}

	//test para comprobar el funcionamiento del show de mis diagnosticos
	@WithMockUser(value = "spring")
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/diagnosis/myDiagnosis")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("diagnosis"))
			.andExpect(MockMvcResultMatchers.model().attribute("diagnosis", Matchers.hasProperty("description", Matchers.is("Prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("diagnosis", Matchers.hasProperty("date", Matchers.is("2020/03/15")))).andExpect(MockMvcResultMatchers.view().name("vets/diagnosisList"));
	}

}
