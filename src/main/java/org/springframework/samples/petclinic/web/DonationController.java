
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause/{causeId}/donations")
public class DonationController {

	@Autowired
	private DonationService	donationService;
	@Autowired
	private CauseService	causeService;


	//	@ModelAttribute("causes")
	//	public Cause findCauseById(HttpServletRequest request){
	//		String causeId = request.getAttribute("causeId").toString();
	//		return this.causeService.findCauseById(Integer.parseInt(causeId));
	//
	//	}

	@GetMapping()
	public String listDonations(final ModelMap modelMap, @PathVariable("causeId") final int causeId) {

		String view = "donations/donationsList";
		Iterable<Donation> donations = this.donationService.findDonationCause(causeId);
		Cause causes = this.causeService.findCauseById(causeId);

		modelMap.addAttribute("causes", causes);
		modelMap.addAttribute("donations", donations);
		return view;

	}

	//	@GetMapping(path = "/new")
	//	public String createDonation(ModelMap modelMap) {
	//
	//		String view = "donations/editDonation";
	//
	//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//		UserDetails userDetails = null;
	//		if (principal instanceof UserDetails) {
	//		  userDetails = (UserDetails) principal;
	//		}
	//		String userName = userDetails.getUsername();
	//		User u = new User();
	//		u.setUsername(userName);
	//
	//		Donation d = new Donation();
	//		d.setUser(u);
	//
	//
	//		modelMap.addAttribute("donation", d);
	//
	//
	//		return view;
	//
	//	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Cause c, final ModelMap model, final HttpServletRequest request, @PathVariable("causeId") final int causeId) {
		String view = "donations/editDonation";
		Donation donation = new Donation();
		c.addDonation(donation);
		
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();
		
		User u = new User();
		u.setUsername(userName);
		
		donation.setUser(u);
		List<String> x = Arrays.asList("true", "false");
		model.addAttribute("anonymous", x);
		Cause causes = this.causeService.findCauseById(causeId);
		String nombre = causes.getTitle();
		donation.setCauses(causes);
		donation.getCauses().setTitle(nombre);

		model.put("donation", donation);
		System.out.println("ESTOY ENTRANDO AQUI CAPI");
		System.out.println(donation.getUser().getUsername());

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

		List<String> x = Arrays.asList("true", "false");
		model.addAttribute("anonymous", x);
		
		if (result.hasErrors()) {
			model.addAttribute("donation", donation);
			return "donations/editDonation";
		} else {
			this.donationService.saveDonation(donation);

			model.addAttribute("message", "Donation successfully saved!");
		}

		return view;
	}

	@GetMapping(path = "/delete/{donationId}")
	public String deleteDonation(@PathVariable("donationId") final int donationId, final ModelMap modelMap, @PathVariable("causeId") final int causeId) {
		String view = "redirect:/cause/{causeId}/donations";

		Optional<Donation> donation = this.donationService.findDonationById(donationId);

		if (donation.isPresent()) {
			this.donationService.delete(donation.get());

			modelMap.addAttribute("message", "Donation successfully deleted!");

		} else {

			modelMap.addAttribute("message", "Donation not found!");
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
