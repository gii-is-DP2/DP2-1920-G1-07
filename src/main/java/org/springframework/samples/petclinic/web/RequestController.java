
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.model.Sitter;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RequestService;
import org.springframework.samples.petclinic.service.SitterService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RequestController {

	private static final String			VIEWS_REQUEST_CREATE_FORM	= "request/requestForm";

	private final PetService			petService;
	private final RequestService		requestService;
	private final UserService			userService;
	private final SitterService			sitterService;
	private final OwnerService			ownerservice;
	private final AuthoritiesService	authoritiesService;


	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@Autowired
	public RequestController(final PetService petService, final AuthoritiesService authoritiesService, final OwnerService ownerservice, final RequestService requestService, final UserService userService, final SitterService sitterService) {
		this.requestService = requestService;
		this.petService = petService;
		this.userService = userService;
		this.sitterService = sitterService;
		this.ownerservice = ownerservice;
		this.authoritiesService = authoritiesService;
	}

	@GetMapping(value = "/request/new")
	public String initCreationForm(final Map<String, Object> model) {
		Request request = new Request();
		model.put("request", request);
		return RequestController.VIEWS_REQUEST_CREATE_FORM;
	}

	@PostMapping(value = "/request/new")
	public String processCreationForm(final HttpServletRequest request, @Valid final Request requestEntity, final BindingResult result, final ModelMap model) {
		if (requestEntity.getType() == null) {
			result.rejectValue("type", "type", "Required");
		}
		Principal principal = request.getUserPrincipal();
		User user = this.userService.findUserByUserName(principal.getName());
		if (result.hasErrors()) {
			return RequestController.VIEWS_REQUEST_CREATE_FORM;
		} else {
			Request r = this.requestService.findRequestByUser(user.getUsername());
			if (r != null) {

				result.rejectValue("address", "address", "You already sent a request");
				return RequestController.VIEWS_REQUEST_CREATE_FORM;
			} else {
				requestEntity.setUser(user);
				this.requestService.saveRequest(requestEntity);
				return "redirect:/";
			}
		}
	}

	@GetMapping(value = "/admin/request")
	public String showRequest(final HttpServletRequest request, final Map<String, Object> model) {
		Collection<Request> requests = this.requestService.findAll();
		model.put("requests", requests);
		return "request/requestList";
	}

	@GetMapping(value = "/admin/request/{requestId}/reject")
	public String rejectRequest(@PathVariable("requestId") final int requestId, final Model model) {
		Request request = this.requestService.findRequestById(requestId);
		this.requestService.deleteRequest(request);
		return "redirect:/admin/request";
	}

	@GetMapping(value = "/admin/request/{requestId}/accept")
	public String acceptRequest(@PathVariable("requestId") final int requestId, final Model model) {
		Request request = this.requestService.findRequestById(requestId);
		Owner owner = this.ownerservice.findOwnerByUser(request.getUser().getUsername());
		Sitter sitter = new Sitter();
		sitter.setAddress(request.getAddress());
		sitter.setTelephone(request.getTelephone());
		sitter.setType(request.getType());
		sitter.setUser(request.getUser());
		sitter.setFirstName(owner.getFirstName());
		sitter.setLastName(owner.getLastName());
		this.requestService.deleteRequest(request);
		this.sitterService.saveSitter(sitter);
		this.authoritiesService.saveAuthorities(request.getUser().getUsername(), "sitter");
		return "redirect:/admin/request";
	}
}
