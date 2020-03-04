
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CauseController {

	private static final String	VIEWS_CAUSE_CREATE_OR_UPDATE_FORM	= "cause/createOrUpdateCauseForm";

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

	@GetMapping(value = {
		"/cause"
	})
	//	final Map<String, Object> model
	public String showCausesList(final ModelMap modelMap) {
		Collection<Cause> causes = this.causeService.findAcceptedCauses();
		modelMap.addAttribute("cause", causes);
		return "causes/causesList";
	}

	@ModelAttribute("user")
	public User findUser(@PathVariable("userId") final int userId) {
		return this.userService.findUserById(userId);
	}

	@GetMapping(value = "/cause/new")
	public String initCreationForm(final Map<String, Object> model) {
		Cause cause = new Cause();
		model.put("cause", cause);
		return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/cause/new")
	public String processCreationForm(@Valid final Cause cause, final User user, final BindingResult result) {
		if (result.hasErrors()) {
			return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
		} else {
			cause.setUser(user);
			cause.setStatus(this.causeService.findPendingStatus());
			this.causeService.saveCauses(cause);
			return "redirect:/";
		}
	}

}
