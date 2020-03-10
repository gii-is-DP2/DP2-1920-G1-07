package org.springframework.samples.petclinic.web;
  
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ehcache.shadow.org.terracotta.offheapstore.util.FindbugsSuppressWarnings;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause/{causeId}/donations")
public class DonationController {
 
	@Autowired
	private DonationService donationService;
	@Autowired 
	private CauseService causeService;
	
	
//	@ModelAttribute("causes")
//	public Cause findCauseById(HttpServletRequest request){
//		String causeId = request.getAttribute("causeId").toString();
//		return this.causeService.findCauseById(Integer.parseInt(causeId));
//		
//	}
		
	@GetMapping()
	public String listDonations(ModelMap modelMap,@PathVariable("causeId") final int causeId) {

		String view = "donations/donationsList";
		Iterable<Donation> donations = donationService.findAllDonations();
		Cause causes = this.causeService.findCauseById(causeId);
		
		
		
		modelMap.addAttribute("causes", causes);
		modelMap.addAttribute("donations", donations);
		return view;
  
	}
	@GetMapping("/myDonations")
	public String listMyDonations(ModelMap modelMap) {

		String view = "donations/donationsList";
		Iterable<Donation> donations = donationService.findAllDonations();
		
		
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
	public String initCreationForm(Cause c, ModelMap model) {
		String view = "donations/editDonation";
		Donation d = new Donation();
		c.addDonation(d);
	
		model.put("donation", d);
		return view;   
	}

	


	@PostMapping(path = "/save")
	public String saveDonation(@Valid Donation donation, BindingResult result, ModelMap modelMap,@PathVariable("causeId") final int causeId) {
		String view = "redirect:/cause/donations";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if (principal instanceof UserDetails) {
		  userDetails = (UserDetails) principal;
		}
		String userName = userDetails.getUsername();
		User u = new User();
		u.setUsername(userName);
		donation.setUser(u);
		
		Cause causes = this.causeService.findCauseById(causeId);
		String nombre = causes.getTitle();
		
		donation.setNombre(nombre);
		
		
		
 
		if (result.hasErrors()) {
			modelMap.addAttribute("donation", donation);
			return "donations/editDonation";
		} else {
			donation.setCauses(causeService.findCauseName(donation.getNombre()));
			donationService.saveDonation(donation);

			modelMap.addAttribute("message", "Donation successfully saved!");
		}

		return view;
	}

	@GetMapping(path = "/delete/{donationId}")
	public String deleteDonation(@PathVariable("donationId") int donationId, ModelMap modelMap) {
		String view = "redirect:/cause/donations";
	
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