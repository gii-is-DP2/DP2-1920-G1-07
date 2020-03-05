package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.User;
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
@RequestMapping("/donations")
public class DonationController {

	@Autowired
	private DonationService donationService;
	

	@GetMapping()
	public String listDonations(ModelMap modelMap) {

		String view = "donations/donationsList";
		Iterable<Donation> donations = donationService.findAllDonations();
		
		
		modelMap.addAttribute("donations", donations);
		return view;

	}

	@GetMapping(path = "/new")
	public String createDonation(ModelMap modelMap) {

		String view = "donations/editDonation";
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if (principal instanceof UserDetails) {
		  userDetails = (UserDetails) principal;
		}
		String userName = userDetails.getUsername();
		User u = new User();
		u.setUsername(userName);
		
		Donation d = new Donation();
		d.setUser(u);
		

		modelMap.addAttribute("donation", d);
		

		return view;
	
	}

	@PostMapping(path = "/save")
	public String saveDonation(@Valid Donation donation, BindingResult result, ModelMap modelMap) {
		String view = "redirect:/donations";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if (principal instanceof UserDetails) {
		  userDetails = (UserDetails) principal;
		}
		String userName = userDetails.getUsername();
		User u = new User();
		u.setUsername(userName);
		donation.setUser(u);

		if (result.hasErrors()) {
			modelMap.addAttribute("donation", donation);
			return "donations/editDonation";
		} else {
			donationService.saveDonation(donation);
			modelMap.addAttribute("message", "Donation successfully saved!");
		}

		return view;
	}

	@GetMapping(path = "/delete/{donationId}")
	public String deleteDonation(@PathVariable("donationId") int donationId, ModelMap modelMap) {
		String view = "redirect:/donations";
	
		Optional<Donation> donation = donationService.findDonationById(donationId);

		if (donation.isPresent()) {
			donationService.delete(donation.get());
			
			modelMap.addAttribute("message", "Donation successfully deleted!");
			
			
		} else {

			modelMap.addAttribute("message", "Donation not found!");
		}

		return view;
	}

}
