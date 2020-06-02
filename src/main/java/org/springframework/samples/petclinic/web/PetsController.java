
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.projections.OwnerPets;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PetsController {

	private static final String	VIEWS_PETS_CREATE_OR_UPDATE_FORM	= "pets/createOrUpdatePetForm";
	private static final String	VIEWS_PETS_LIST_ADMIN				= "pets/petListAdmin";
	private static final String	REDIRECT_OWNER_PETS	= "redirect:/owner/pets";

	private final PetService	petService;
	private final OwnerService	ownerService;


	@Autowired
	public PetsController(final PetService petService, final OwnerService ownerService) {
		this.petService = petService;
		this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping(value = "/admin/pets")
	public String showPetsAdmin(final HttpServletRequest request, final ModelMap model) {
		Collection<Pet> pets = this.petService.findAll();
		model.put("pets", pets);
		return PetsController.VIEWS_PETS_LIST_ADMIN;
	}

	@GetMapping(value = "/owner/pets")
	public ModelAndView showPets(final HttpServletRequest request, final ModelMap model) {
		/*
		 * Añado el atributo wasSavedOnGoogleCalendar al model para que en caso de que ya se haya guardado
		 * la visita de una pet en google calendar ya no salga mas el boton para volver a hacerlo.
		 */
		String accessToken = (String) request.getSession().getAttribute("accessToken");
		model.addAttribute("haceAccessToken", accessToken != null);
		Principal principal = request.getUserPrincipal();
		ModelAndView mav = new ModelAndView("pets/petList");
		Collection<OwnerPets> ow = this.petService.findOwnerPetsById(principal.getName());
		Owner owner = new Owner();
		owner.setId(this.ownerService.findIdByName(principal.getName()));
		for (OwnerPets p : ow) {
			if (owner.getPets().stream().anyMatch(X -> X.getId().equals(p.getId()))) {
				if (p.getVisitDate() != null) {
					Visit v = new Visit();
					v.setDate(p.getVisitDate());
					v.setDescription(p.getVisitDescription());
					owner.getPets().stream().filter(X -> X.getId().equals(p.getId())).findFirst().get().addVisit(v);
				}
			} else {
				Pet pet = new Pet();
				pet.setName(p.getName());
				PetType type = new PetType();
				type.setName(p.getType());
				pet.setType(type);
				pet.setBirthDate(p.getBirthDate());
				Integer id = p.getId();
				pet.setId(id);

				if (p.getVisitDate() != null) {
					Visit v = new Visit();
					v.setDate(p.getVisitDate());
					v.setDescription(p.getVisitDescription());
					pet.addVisit(v);
				}

				owner.addPet(pet);

			}
		}
		mav.addObject(owner);
		return mav;
	}

	@GetMapping(value = "/owner/pets/new")
	public String initCreationForm(final HttpServletRequest request, final ModelMap model) {
		Principal principal = request.getUserPrincipal();
		Owner owner = this.ownerService.findOwnerByUser(principal.getName());
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owner/pets/new")
	public String processCreationForm(final HttpServletRequest request, @Valid final Pet pet, final BindingResult result, final ModelMap model) {
		Principal principal = request.getUserPrincipal();
		Owner owner = this.ownerService.findOwnerByUser(principal.getName());
		PetValidator v = new PetValidator();
		v.validate(pet, result);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			try {
				owner.addPet(pet);
				this.petService.savePet(pet);
			} catch (DuplicatedPetNameException ex) {
				result.rejectValue("name", "duplicate", "already exists");
				return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			}
			return REDIRECT_OWNER_PETS;
		}
	}

	@GetMapping(value = "/owner/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") final int petId, final ModelMap model) {
		Pet pet = this.petService.findPetById(petId);
		model.put("pet", pet);
		return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	/**
	 *
	 * @param pet
	 * @param result
	 * @param petId
	 * @param model
	 * @param owner
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/owner/pets/{petId}/edit")
	public String processUpdateForm(@Valid final Pet pet, final BindingResult result, final Owner owner, @PathVariable("petId") final int petId, final ModelMap model) {
		PetValidator v = new PetValidator();
		v.validate(pet, result);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			Pet petToUpdate = this.petService.findPetById(petId);
			BeanUtils.copyProperties(pet, petToUpdate, "id", "owner", "visits");
			try {
				this.petService.savePet(petToUpdate);
			} catch (DuplicatedPetNameException ex) {
				result.rejectValue("name", "duplicate", "already exists");
				return PetsController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			}
			return REDIRECT_OWNER_PETS;
		}
	}

	@GetMapping(value = "/owner/pets/{petId}/delete")
	public String initDelete(@PathVariable("petId") final int petId, final ModelMap model) {
		Pet pet = this.petService.findPetById(petId);
		this.petService.deletePet(pet);
		return REDIRECT_OWNER_PETS;
	}
}
