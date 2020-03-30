
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiagnosisController {

	private static final String		VIEWS_DIAGNOSIS_CREATE_FORM	= "vets/createDiagnosis";

	private final DiagnosisService	diagnosisService;

	private final VetService		vetService;

	private final VisitService		visitService;


	@Autowired
	public DiagnosisController(final DiagnosisService clinicService, final VetService vetService, final VisitService visitService) {
		this.diagnosisService = clinicService;
		this.vetService = vetService;
		this.visitService = visitService;
	}

	@GetMapping(value = "/vet/{vetId}/diagnosis")
	public String initCreationForm(final ModelMap model) {
		Diagnosis diagnosis = new Diagnosis();
		model.put("diagnosis", diagnosis);
		return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
	}

	@PostMapping(value = "/vet/{vetId}/diagnosis")
	public String processCreateDiagnosis(@Valid final Diagnosis diagnosis, final BindingResult result, final ModelMap model, @PathVariable("vetId") final int vetId, @RequestParam("visitId") final int visitId) {
		if (diagnosis.getDate() == null) {
			result.rejectValue("date", "Date must not be null");
		}

		if (result.hasErrors()) {
			model.put("message", "Diagnosis not created");
			model.put("diagnosis", diagnosis);
			return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
		} else {
			Vet v = this.vetService.findVetById(vetId);

			Visit visit = this.visitService.findById(visitId);

			diagnosis.setVet(v);
			diagnosis.setVisit(visit);
			diagnosis.setPet(visit.getPet());
			this.diagnosisService.saveDiagnosis(diagnosis);
			model.put("message", "Diagnosis succesfully created");
		}
		return "redirect:/vets/mySpace/";
	}

	@GetMapping(path = "/diagnosis/myDiagnosis")
	public String showMyDiagnosisList(final ModelMap model, final HttpServletRequest request, @RequestParam("petId") final int petId) {
		Collection<Diagnosis> myDiagnosis = this.diagnosisService.findMyDiagnosis(petId);
		model.put("diagnosis", myDiagnosis);
		return "vets/diagnosisList";
	}

}
