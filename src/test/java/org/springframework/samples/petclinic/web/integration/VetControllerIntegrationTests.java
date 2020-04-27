
package org.springframework.samples.petclinic.web.integration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetControllerIntegrationTests {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_SPECIALTIES_ID	= 1;

	private static final int	TEST_OWNER_ID		= 1;

	private static final int	TEST_PET_ID			= 1;

	private static final String	TEST_USER_NAME		= "spring";

	@Autowired
	private VetController		vetController;

	@MockBean
	private VetService			clinicService;


	@Test
	void testInitCreationForm() throws Exception {
		ModelMap model = new ModelMap();

		String view = this.vetController.initCreationForm(model);

		Assertions.assertEquals(view, "vets/createVet");

	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		Vet vet = new Vet();
		vet.setId(VetControllerIntegrationTests.TEST_VET_ID);
		vet.setFirstName("Pablo");
		vet.setLastName("Reneses");
		int[] specialties = {
			1
		};

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.vetController.processCreationForm(vet, bindingResult, specialties);

		Assertions.assertEquals(view, "redirect:/vets/admin");

	}

	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		Vet vet = new Vet();
		vet.setFirstName("Pablo");
		int[] specialties = {};
		//		for (int i : specialties) {
		//			Specialty s = this.clinicService.findSpecialiesById(i);
		//			vet.addSpecialty(s);
		//		}
		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		bindingResult.reject("vet", "Requied!");

		String view = this.vetController.processCreationForm(vet, bindingResult, specialties);

		Assertions.assertEquals(view, "vets/createVet");
	}

	@Test
	void testShow() throws Exception {

		Map<String, Object> model = new HashMap<String, Object>();
		MockHttpServletRequest request = new MockHttpServletRequest();

		String view = this.vetController.visitList(model, request);

		Assertions.assertEquals(view, "vets/visitList");
	}

}
