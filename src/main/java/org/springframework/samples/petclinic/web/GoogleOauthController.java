package org.springframework.samples.petclinic.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

@Controller
public class GoogleOauthController extends HttpServlet {

	/**
	* "clientId":  "248454269324-lk4je2a5hsmvi7lbsg3eb3shlt7lc4lh.apps.googleusercontent.com",
        "clientSecret": "GBHHTb3wFMiPsHHcb09tA-pX",
        "authorizationFormUrl": "https://accounts.google.com/o/oauth2/v2/auth",
        "scopes": ["https://www.googleapis.com/auth/calendar"]
	*/
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GoogleOauthController.class.getName());
	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;
	
	@Value("${google.client.client-id}")
	private String clientId;
	@Value("${google.client.client-secret}")
	private String clientSecret;
	@Value("${google.client.redirectUri}")
	private String redirectUrl;

	@GetMapping(value="/singIn/google")
	public RedirectView googleConnectionToOauth2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new RedirectView(authorize());
		
	}
	
	private String authorize() throws IOException, GeneralSecurityException {
		HttpTransport httptransport;
		AuthorizationCodeRequestUrl auRequestUrl;
		if(flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httptransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httptransport, JSON_FACTORY, clientId, clientSecret, Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		auRequestUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUrl);
		log.fine("cal authorizationUrl->" + auRequestUrl);
		return auRequestUrl.build();
	}
	
	@GetMapping(value="/singIn/google",params = "code")
	public String oauth2CallBack(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code,ModelMap model) {

		try {
			TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUrl).execute();
			request.getSession().setAttribute("accessToken", tokenResponse.getAccessToken());
			
		}catch (Exception e) {
			// TODO: handle exception
			log.warning("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
			+ " Redirecting to google connection status page.");
		}
		
		return "redirect:/owner/pets";
	}
	
}
