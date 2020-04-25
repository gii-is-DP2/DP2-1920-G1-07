package org.springframework.samples.petclinic.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.PaypalPaymentIntent;
import org.springframework.samples.petclinic.configuration.PaypalPaymentMethod;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.PayPalClient;
import org.springframework.samples.petclinic.model.paypal.Amount;
import org.springframework.samples.petclinic.model.paypal.PurchaseUnit;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.PaypalService;
import org.springframework.samples.petclinic.util.URLUtils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
@RequestMapping("/paypal")
public class PaypalController {
	
	public static final String PAYPAL_SUCCEESS_URL = "paypal/success";
	public static final String PAYPAL_CANCEL_URL = "paypal/cancel";
	
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private PaypalService paypalService;
	@Autowired
	private DonationService	donationService;
	
	
	@GetMapping(value="/make/payment")
	public String makePayment(@RequestParam("donationId")int donationId,ModelMap model) {
		String view = "paypal/check";
		Donation d = this.donationService.findDonationByIdNotOptional(donationId);
		model.put("donation", d);
		return view;
	}
	
	@PostMapping(value="/make/payment")
	public String makePayment(@Valid final Donation donation,@RequestParam("money") String money,@RequestParam("currency") String currency,HttpServletRequest request) throws IOException{
		String cancelUrl = URLUtils.getBaseURl(request)+"/"+PAYPAL_CANCEL_URL;
		String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCEESS_URL;
		Double mo = donation.getMoney();
		mo = 1000.00;
		try {
			Payment payment = paypalService.createPayment(999.98, "EUR", PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect: "+link.getHref();
				}
			}
		}catch (PayPalRESTException e) {
			BufferedReader r = request.getReader();
			log.error(e.getMessage());
		}
		return "redirect:/cause";
	}
	@GetMapping(value=""+PAYPAL_CANCEL_URL)
	public String cancelPay() {
		return "causes/causesList";
	}
	
	@PostMapping(value="/complete/payment")
	public String completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PlayerID") String playerId) { 
		try {
			Payment payment = paypalService.completePayment(paymentId, playerId);
			if(payment.getState().equals("approved")) {
				return "causes/causesList";
			}
		}catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "causes/causesList";
	}
	
}
