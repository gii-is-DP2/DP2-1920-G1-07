package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.exception.PayPalException;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Component
public class PayPalClient {

	String clientId = "AWErcZz4WGKg074BvDNsHIenF6C3WhBG7xImV0fV8xDnBJRs-gIYZM7-I4XW5SGAc0n_RsiAKPBkeOWo";
	String secretId = "EC1YhY7D7nzqBWDCBuRuae-tXqi3wdClWfP7niGcRtCT6KZAPrxsF5n4wKmKouTzb2vUviZ7ZI0b0EE4";

}
