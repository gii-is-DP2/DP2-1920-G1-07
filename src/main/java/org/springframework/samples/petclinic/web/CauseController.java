
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CauseController {

	private static final String	VIEWS_CAUSE_CREATE_OR_UPDATE_FORM	= "causes/createOrUpdateCauseForm";
	private static final String	VIEWS_CAUSE_PENDING_UPDATE_FORM		= "causes/updatePendingCauseForm";
	private static final String	CAUSE		= "cause";

	private final CauseService	causeService;
	private final UserService	userService;


	@Autowired
	public CauseController(final CauseService causeService, final UserService userService) {
		this.causeService = causeService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder(CAUSE)
	public void initCauseBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new CauseValidator());
	}

	@GetMapping(path = "/cause")
	public String showCausesList(final ModelMap model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		model.put("userName", userName);

		Collection<Cause> causes = this.causeService.findAcceptedCauses();
		Collection<Cause> causasValidas = new ArrayList<Cause>();
		LocalDate now = LocalDate.now();
		for (Cause c : causes) {
			if (c.getDeadline().isAfter(now)) {
				causasValidas.add(c);
			}
		}
		model.addAttribute(CAUSE, causasValidas);
		return "causes/causesList";
	}

	@GetMapping(path = "/cause/myCauses/{userName}")
	public String showMyCausesList(@PathVariable("userName") final String username, final ModelMap model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		model.put("userName", userName);
		Collection<Cause> myCauses = this.causeService.findMyCauses(userName);

		if (!userName.equals(username)) {
			String mensaje = "No puedes ver las causas que ha realizado otra persona";
			model.put("mensaje", mensaje);
			return "causes/myCausesList";
		}

		model.addAttribute("causes", myCauses);
		return "causes/myCausesList";
	}

	@GetMapping(path = "/cause/new")
	public String initCreationForm(final ModelMap model) {
		Cause cause = new Cause();
		model.addAttribute(CAUSE, cause);
		return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/cause/new")
	public String processCreationForm(@Valid final Cause cause, final BindingResult result, final ModelMap model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		User u = this.userService.findUserByUserName(userName);

		cause.setUser(u);

		if (result.hasErrors()) {
			return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
		} else {
			cause.setStatus(this.causeService.findPendingStatus());
			this.causeService.saveCauses(cause);
			return "redirect:/cause";
		}
	}

	@GetMapping(path = "/causes/PendingCauses")
	public String showPendingCausesList(final ModelMap model) {
		Collection<Cause> myCauses = this.causeService.findPendingCauses();
		model.addAttribute(CAUSE, myCauses);
		return "causes/pendingCauses";
	}

	@ModelAttribute("status")
	public Collection<Status> populateStatus() {
		return this.causeService.findStatus();
	}

	@GetMapping(value = "/causes/PendingCauses/cause/{causeId}/edit")
	public String initUpdateOwnerForm(@PathVariable("causeId") final int causeId, final ModelMap model) {
		Cause cause = this.causeService.findCauseById(causeId);
		model.put(CAUSE, cause);
		return CauseController.VIEWS_CAUSE_PENDING_UPDATE_FORM;
	}

	@PostMapping(value = "/causes/PendingCauses/cause/{causeId}/edit")
	public String processUpdateOwnerForm(@Valid final Cause cause, final BindingResult result, @PathVariable("causeId") final int causeId, final ModelMap model) {
		Cause causeToUpdate = this.causeService.findCauseById(causeId);
		causeToUpdate.setStatus(cause.getStatus());
		this.causeService.saveCauses(causeToUpdate);
		return "redirect:/causes/PendingCauses";

	}

}
