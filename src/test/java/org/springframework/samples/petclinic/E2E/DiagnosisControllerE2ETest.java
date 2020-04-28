
package org.springframework.samples.petclinic.E2E;

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
public class DiagnosisControllerE2ETest {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_DIAGNOSIS_ID	= 1;

	private static final int	TEST_VISIT_ID		= 1;

	private static final int	TEST_PET_ID			= 1;

	@Autowired
	private MockMvc				mockMvc;


	//test para que te lleve al formulario de creación de diagnositico
	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vet/{vetId}/diagnosis", DiagnosisControllerE2ETest.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("diagnosis"))
			.andExpect(MockMvcResultMatchers.view().name("vets/createDiagnosis"));
	}

	//test para comprobar la creacion de un diagnostico correctamente
	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vet/{vetId}/diagnosis?visitId={visitId}", DiagnosisControllerE2ETest.TEST_VET_ID, DiagnosisControllerE2ETest.TEST_VISIT_ID).param("description", "Prueba").param("date", "2025/03/15")
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/mySpace/"));
	}

	//test de creacion de un diagnostico con fallo, ya que faltaria poner date
	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/vet/{vetId}/diagnosis?visitId={visitId}", DiagnosisControllerE2ETest.TEST_VET_ID, DiagnosisControllerE2ETest.TEST_VISIT_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Prueba"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("diagnosis")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("diagnosis", "date"))
			.andExpect(MockMvcResultMatchers.view().name("vets/createDiagnosis"));
	}

	//test para comprobar el funcionamiento del show de mis diagnosticos
	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testShowDiagnosis() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/diagnosis/myDiagnosis").param("petId", "1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("diagnosis"))
			//Una forma alternativa de asegurar que el modelo tenga el diagnostico con los datos dentro
			.andExpect(MockMvcResultMatchers.model().size(1)).andExpect(MockMvcResultMatchers.view().name("vets/diagnosisList"));
	}
}
