
package org.springframework.samples.petclinic.web.integration;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.DiagnosisController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiagnosisControllerIntegrationTests {

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


	//test para que te lleve al formulario de creación de diagnositico
	@Test
	void testInitCreationForm() throws Exception {

		ModelMap model = new ModelMap();

		String view = this.diagnosisController.initCreationForm(model);
		Assertions.assertEquals(view, "vets/createDiagnosis");

	}

	//test para comprobar la creacion de un diagnostico correctamente
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Diagnosis diagnosis = new Diagnosis();

		diagnosis.setDate(LocalDate.now());
		diagnosis.setDescription("Prueba");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.diagnosisController.processCreateDiagnosis(diagnosis, bindingResult, model, DiagnosisControllerIntegrationTests.TEST_VET_ID, DiagnosisControllerIntegrationTests.TEST_VISIT_ID);

		Assertions.assertEquals(view, "redirect:/vets/mySpace/");
	}
	//test de creacion de un diagnostico con fallo, ya que faltaria poner date
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Diagnosis diagnosis = new Diagnosis();

		diagnosis.setDescription("Prueba");
		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		bindingResult.reject("date", "Requied!");
		String view = this.diagnosisController.processCreateDiagnosis(diagnosis, bindingResult, model, DiagnosisControllerIntegrationTests.TEST_VET_ID, DiagnosisControllerIntegrationTests.TEST_VISIT_ID);

		Assertions.assertEquals(view, "vets/createDiagnosis");

	}
	//test para comprobar el funcionamiento del show de mis diagnosticos
	@Test
	void testShowDiagnosis() throws Exception {
		ModelMap model = new ModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();

		String view = this.diagnosisController.showMyDiagnosisList(model, request, DiagnosisControllerIntegrationTests.TEST_PET_ID);

		Assertions.assertEquals(view, "vets/diagnosisList");
	}
}
