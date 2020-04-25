package org.springframework.samples.petclinic.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;


@Configuration
public class PaypalConfig {
	
//	@Value("${paypal.client.app}")
	private String clientId = "AWErcZz4WGKg074BvDNsHIenF6C3WhBG7xImV0fV8xDnBJRs-gIYZM7-I4XW5SGAc0n_RsiAKPBkeOWo";
//	@Value("${paypal.client.secret")
	private String clientSecret = "EC1YhY7D7nzqBWDCBuRuae-tXqi3wdClWfP7niGcRtCT6KZAPrxsF5n4wKmKouTzb2vUviZ7ZI0b0EE4";
//	@Value("${paypal.mode}")
	private String mode = "sandbox"; 
	
	
	@Bean
	public Map<String,String> paypalSdkConfig() {
		Map<String,String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", mode);
		sdkConfig.put("currency","EUR");
		return sdkConfig;
	}
	
	@Bean
	public OAuthTokenCredential authTokenCredential() {
		return new OAuthTokenCredential(clientId,clientId, paypalSdkConfig());
	}
	
	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
		apiContext.fetchAccessToken();
		apiContext.setConfigurationMap(paypalSdkConfig());
		return apiContext;
	}
	
}
