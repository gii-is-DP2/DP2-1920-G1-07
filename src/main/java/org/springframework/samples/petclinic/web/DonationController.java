package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/donations")
public class DonationController {
	
	@Autowired
	private DonationService donationService;
	
	@GetMapping()
	public String listDonations(ModelMap modelMap) {
		
		String view ="donations/donationsList";
		Iterable<Donation> donations = donationService.findAllDonations();
		modelMap.addAttribute("donations",donations);
		return view;
		
	}

}
