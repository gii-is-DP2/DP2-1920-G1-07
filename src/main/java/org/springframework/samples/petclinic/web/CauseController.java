
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause")
public class CauseController {

	private static final String	VIEWS_CAUSE_CREATE_OR_UPDATE_FORM	= "causes/createOrUpdateCauseForm";

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

	@GetMapping("/myCauses")
	public String showMyCausesList(final ModelMap modelMap, final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		Collection<Cause> myCauses = this.causeService.findMyCauses(userName);

		modelMap.addAttribute("causes", myCauses);
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

		cause.setUser(u);

		if (result.hasErrors()) {
			return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
		} else {
			cause.setStatus(this.causeService.findPendingStatus());
			this.causeService.saveCauses(cause);
			return "redirect:/cause";
		}
	}

}
