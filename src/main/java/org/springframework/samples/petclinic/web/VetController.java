/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private static final String	VIEWS_VET_CREATE_FORM	= "vets/createVet";
	private static final String	VIEWS_VET_LIST			= "vets/visitList";

	private final VetService	vetService;


	@Autowired
	public VetController(final VetService clinicService) {
		this.vetService = clinicService;
	}

	//	@GetMapping(value = {
	//		"/vets"
	//	})
	//	public String showVetList(final Map<String, Object> model, @RequestParam("petId") final int petId) {
	//		// Here we are returning an object of type 'Vets' rather than a collection of Vet
	//		// objects
	//		// so it is simpler for Object-Xml mapping
	//		Vets vets = new Vets();
	//		vets.getVetList().addAll(this.vetService.findVets());
	//		model.put("vets", vets);
	//		model.put("petId", petId);
	//		return "vets/vetList";
	//	}
	//
	//	@GetMapping(value = "/vets/admin")
	//	public String showVetListForAdmin(final Map<String, Object> model) {
	//		// Here we are returning an object of type 'Vets' rather than a collection of Vet
	//		// objects
	//		// so it is simpler for Object-Xml mapping
	//		Vets vets = new Vets();
	//		vets.getVetList().addAll(this.vetService.findVets());
	//		model.put("vets", vets);
	//		return "vets/vetListAdmin";
	//	}

	@GetMapping(value = "/vets/create")
	public String initCreationForm(final ModelMap model) {
		Vet vet = new Vet();
		vet.setVisits(new HashSet<>());
		model.put("vet", vet);
		return VetController.VIEWS_VET_CREATE_FORM;
	}

	@PostMapping(value = "/vets/create")
	public String processCreationForm(@Valid final Vet vet, final BindingResult result, @RequestParam(required = false) final int[] specialties) {
		//		Vet vet2 = this.vetService.findVetById(vet.getId());
		//		String r = vet2.getFirstName();
		//		String t = vet2.getLastName();
		if (result.hasErrors()) {
			return VetController.VIEWS_VET_CREATE_FORM;
		} else {
			for (int i : specialties) {
				Specialty s = this.vetService.findSpecialiesById(i);
				vet.addSpecialty(s);
			}
			//			this.authService.saveAuthorities(vet.getUser().getUsername(), "veterinarian");
			if(vet.getUser() != null) {
			vet.getUser().setEnabled(true);
			}
			this.vetService.saveVet(vet);
		}

		return "redirect:/vets/admin";
	}

	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findSpecialties();
	}

	@GetMapping(value = "/vets/mySpace")
	public String visitList(final Map<String, Object> model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		model.put("userName", userName);

		Vet vet = this.vetService.findVetByUserName(userName);
		model.put("vet", vet);

		Collection<Visit> v = this.vetService.findVisits(userName);
		model.put("visits", v);

		Collection<Diagnosis> d = this.vetService.findDiagnosis(userName);
		model.put("diagnosis", d);

		return VetController.VIEWS_VET_LIST;
	}

}
