
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause")
public class CauseController {

	private static final String	VIEWS_CAUSE_CREATE_OR_UPDATE_FORM	= "causes/createOrUpdateCauseForm";
	private static final String	VIEWS_CAUSE_PENDING_UPDATE_FORM		= "causes/updatePendingCauseForm";

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

	@GetMapping
	public String showCausesList(final ModelMap modelMap) {
		Collection<Cause> causes = this.causeService.findAcceptedCauses();
		modelMap.addAttribute("cause", causes);
		return "causes/causesList";
	}

	@GetMapping(path = "/myCauses")
	public String showMyCausesList(final ModelMap model, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		model.put("userName", userName);

		Collection<Cause> myCauses = this.causeService.findMyCauses(userName);

		model.addAttribute("causes", myCauses);
		return "causes/myCausesList";
	}

	@GetMapping(path = "/new")
	public String initCreationForm(final ModelMap model) {
		Cause cause = new Cause();
		model.addAttribute("cause", cause);
		return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Cause cause, final BindingResult result, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		User u = this.userService.findUserByUserName(userName);

		LocalDate deadline = cause.getDeadline();
		LocalDate now = LocalDate.now();
		Boolean posterior = deadline.isBefore(now);

		cause.setUser(u);

		if (result.hasErrors() || posterior) {
			return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
		} else {
			cause.setStatus(this.causeService.findPendingStatus());
			this.causeService.saveCauses(cause);
			return "redirect:/cause";
		}
	}

	@GetMapping(path = "/PendingCauses")
	public String showPedingCausesList(final ModelMap model) {
		Collection<Cause> myCauses = this.causeService.findPendingCauses();
		model.addAttribute("cause", myCauses);
		return "causes/pendingCauses";
	}

	@ModelAttribute("status")
	public Collection<Status> populateStatus() {
		return this.causeService.findStatus();
	}

	@GetMapping(value = "/PendingCauses/cause/{causeId}/edit")
	public String initUpdateOwnerForm(@PathVariable("causeId") final int causeId, final ModelMap model) {
		Cause cause = this.causeService.findCauseById(causeId);
		model.put("cause", cause);
		return CauseController.VIEWS_CAUSE_PENDING_UPDATE_FORM;
	}

	@PostMapping(value = "/PendingCauses/cause/{causeId}/edit")
	public String processUpdateOwnerForm(@Valid final Cause cause, final BindingResult result, @PathVariable("causeId") final int causeId, final ModelMap model) {
		Cause causeToUpdate = this.causeService.findCauseById(causeId);
		causeToUpdate.setStatus(cause.getStatus());
		this.causeService.saveCauses(causeToUpdate);
		return "redirect:/cause/PendingCauses";

	}

}
