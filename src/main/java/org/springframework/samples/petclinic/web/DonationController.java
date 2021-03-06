
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause/{causeId}/donations")
public class DonationController {

	private static final String	DONATION	= "donation";
	private static final String	MESSAGE		= "message";

	@Autowired
	private DonationService		donationService;
	@Autowired
	private CauseService		causeService;


	@InitBinder(DonationController.DONATION)
	public void initDonationBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new DonationValidator());
	}

	@GetMapping()
	public String listDonations(final ModelMap modelMap, @PathVariable("causeId") final int causeId) {
		 String view = "donations/donationsList";
		 Iterable<Donation> donations = this.donationService.findDonationCause(causeId);
		 Cause causes = this.causeService.findCauseById(causeId);

		 modelMap.addAttribute("causes", causes);
		 modelMap.addAttribute("donations", donations);
		 return view;

	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Cause c, final ModelMap model, final HttpServletRequest request, @PathVariable("causeId") final int causeId) {
		String view = "donations/editDonation";
		Donation donation = new Donation();
		c.addDonation(donation);

		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		User u = new User();
		u.setUsername(userName);

		Cause causes = this.causeService.findCauseById(causeId);
		Collection<Donation> ds = this.donationService.findDonationCause(causeId);
		Double moneyD = 0.;
		Double moneyF = causes.getMoney();

		if (!ds.isEmpty()) {

			moneyD = ds.stream().mapToDouble(x -> x.getMoney()).sum();

		}
		Double moneyRest = moneyF - moneyD;
		donation.setMoneyRest(moneyRest);

		donation.setUser(u);
		List<String> x = Arrays.asList("true", "false");
		model.addAttribute("anonymous", x);
		String nombre = causes.getTitle();
		donation.setCauses(causes);
		donation.getCauses().setTitle(nombre);

		model.put(DonationController.DONATION, donation);

		return view;

	}

	@PostMapping(path = "/new")
	public String saveDonation(@Valid final Donation donation, final BindingResult result, final ModelMap model, final HttpServletRequest request, @PathVariable("causeId") final int causeId) {
		String view = "redirect:/cause/{causeId}/donations";

		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		User u = new User();
		u.setUsername(userName);
		donation.setUser(u);

		Cause causes = this.causeService.findCauseById(causeId);
		String nombre = causes.getTitle();
		donation.setCauses(causes);
		donation.getCauses().setTitle(nombre);

		Collection<Donation> ds = this.donationService.findDonationCause(causeId);
		Double moneyD = 0.;
		Double moneyF = causes.getMoney();

		if (!ds.isEmpty()) {

			moneyD = ds.stream().mapToDouble(x -> x.getMoney()).sum();

		}
		Double moneyRest = moneyF - moneyD;
		donation.setMoneyRest(moneyRest);

		if (donation.getMoney() != null && donation.getMoney() > moneyRest) {
			FieldError err = new FieldError(DonationController.DONATION, "money", "Your donation must not exceed the reamaining money");
			result.addError(err);
		}

		List<String> x = Arrays.asList("true", "false");
		model.addAttribute("anonymous", x);

		if (result.hasErrors()) {
			model.addAttribute(DonationController.DONATION, donation);
			return "donations/editDonation";
		} else {
			this.donationService.saveDonation(donation);

			model.addAttribute(DonationController.MESSAGE, "Donation successfully saved!");
		}

		return view;
	}

	@GetMapping(path = "/delete/{donationId}")
	public String deleteDonation(@PathVariable("donationId") final int donationId, final HttpServletRequest request, final ModelMap modelMap, @PathVariable("causeId") final int causeId) {
		String view = "redirect:/cause/{causeId}/donations";

		Optional<Donation> donation = this.donationService.findDonationById(donationId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authority = "";
		for (GrantedAuthority a : auth.getAuthorities()) {
			authority = a.getAuthority();
		}
		if (!authority.equals("admin")) {
			return "redirect:/oups";
		}

		if (donation.isPresent()) {
			this.donationService.delete(donation.get());

			modelMap.addAttribute(DonationController.MESSAGE, "Donation successfully deleted!");

		} else {

			modelMap.addAttribute(DonationController.MESSAGE, "Donation not found!");
		}

		return view;
	}

	@GetMapping("/myDonations")
	public String showMyDonationsList(final ModelMap modelMap, final HttpServletRequest request) {
		String view = "donations/myDonationsList";
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		Collection<Donation> myDonations = this.donationService.findMyDonations(userName);

		modelMap.addAttribute("donations", myDonations);
		return view;
	}

}
