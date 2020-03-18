
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
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
	
	private final VetService vetService;
	
	private final VisitRepository visitRepository;


	@Autowired
	public DiagnosisController(final DiagnosisService clinicService, VetService vetService,VisitRepository visitRepository) {
		this.diagnosisService = clinicService;
		this.vetService = vetService;
		this.visitRepository = visitRepository;
	}

	@GetMapping(value = "/vet/{vetId}/diagnosis")
	public String initCreationForm(final ModelMap model) {
		Diagnosis diagnosis = new Diagnosis();
		model.put("diagnosis", diagnosis);
		return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
	}

	@PostMapping(value = "/vet/{vetId}/diagnosis")
	public String processCreateDiagnosis(@Valid final Diagnosis diagnosis, final BindingResult result, final ModelMap model,@PathVariable("vetId") int vetId,@RequestParam("visitId") int visitId) {
		if (result.hasErrors()) {
			model.addAttribute("message", "Diagnosis not created");
			model.addAttribute("diagnosis", diagnosis);
			return DiagnosisController.VIEWS_DIAGNOSIS_CREATE_FORM;
		} else {
			Vet v = this.vetService.findVetById(vetId);
			Visit visit = this.visitRepository.findById(visitId);
			diagnosis.setVet(v);
			diagnosis.setVisit(visit);
			diagnosis.setPet(visit.getPet());
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
	public String showMyDiagnosisList(final ModelMap model, final HttpServletRequest request,@RequestParam("petId") int petId) {
		Collection<Diagnosis> myDiagnosis = this.diagnosisService.findMyDiagnosis(petId);
		model.addAttribute("diagnosis", myDiagnosis);
		return "vets/diagnosisList";
	}

}
