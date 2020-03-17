
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DiagnosisController {

	private static final String		VIEWS_DIAGNOSIS_CREATE_FORM	= "vets/createDiagnosis";

	private final DiagnosisService	diagnosisService;


	@Autowired
	public DiagnosisController(final DiagnosisService clinicService) {
		this.diagnosisService = clinicService;
	}

	@GetMapping(value = "/vet/{vetId}/diagnosis")
	public String initCreationForm(final ModelMap model) {
		Diagnosis diagnosis = new Diagnosis();
		model.put("diagnosis", diagnosis);
		return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
	}

	@PostMapping(value = "/vet/{vetId}/diagnosis")
	public String processCreateDiagnosis(@Valid final Diagnosis diagnosis, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("message", "Diagnosis not created");
			model.addAttribute("diagnosis", diagnosis);
			return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
		} else {
			this.diagnosisService.saveDiagnosis(diagnosis);
			model.addAttribute("message", "Diagnosis succesfully created");
		}
		return "redirect:/vets/mySpace/";
	}

	//	@GetMapping
	//	public String showDiagnosisList(final ModelMap modelMap) {
	//		Collection<Diagnosis> diagnosis = this.diagnosisService.findDiagnosis();
	//		modelMap.addAttribute("diagnosis", diagnosis);
	//		return "vets/diagnosisList";
	//	}

	@GetMapping(path = "/diagnosis/myDiagnosis")
	public String showMyDiagnosisList(final ModelMap model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		model.put("userName", userName);

		Collection<Diagnosis> myDiagnosis = this.diagnosisService.findMyDiagnosis(userName);

		model.addAttribute("diagnosis", myDiagnosis);
		return "vets/diagnosisList";
	}

}
