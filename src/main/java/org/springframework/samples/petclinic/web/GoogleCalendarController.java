package org.springframework.samples.petclinic.web;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.googleCalendar.End;
import org.springframework.samples.petclinic.model.googleCalendar.Event;
import org.springframework.samples.petclinic.model.googleCalendar.Start;
import org.springframework.samples.petclinic.resources.GoogleCalendarResources;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

@Controller
public class GoogleCalendarController extends HttpServlet {

	/**
	* "clientId":  "248454269324-lk4je2a5hsmvi7lbsg3eb3shlt7lc4lh.apps.googleusercontent.com",
        "clientSecret": "GBHHTb3wFMiPsHHcb09tA-pX",
        "authorizationFormUrl": "https://accounts.google.com/o/oauth2/v2/auth",
        "scopes": ["https://www.googleapis.com/auth/calendar"]
	*/
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GoogleCalendarController.class.getName());
	private static HttpTransport httptransport;
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
	
	@Autowired
	private VisitService visitService;

	@GetMapping(value="/singIn/google", params = "visitId")
	public RedirectView googleConnectionToOauth2(@RequestParam("visitId") int visitId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("visitId", visitId);
		return new RedirectView(authorize(request));
	}
	
	private String authorize(HttpServletRequest request) throws Exception {
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
		System.out.println("cal authorizationUrl->" + auRequestUrl);
		return auRequestUrl.build();
	}
	
	@GetMapping(value="/singIn/google",params = "code")
	public String oauth2CallBack(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code) {
		try {
			TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUrl).execute();
			request.getSession().setAttribute("accessToken", tokenResponse.getAccessToken());
		}catch (Exception e) {
			// TODO: handle exception
			log.warning("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
			+ " Redirecting to google connection status page.");
		}
		
		String visitId = request.getSession().getAttribute("visitId").toString();
		int intVisitId = Integer.parseInt(visitId);
		return "redirect:/visit/"+intVisitId+"/event/insert/";
	}
	
	@GetMapping(value = "/visit/{visitId}/event/insert/")
	public String initInsertEvent(@PathVariable("visitId") int visitId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String view = "event/initEvent";
		Visit v = this.visitService.findById(visitId);
		String date = v.getDate().toString();
		String dateTimeStart = date.concat("T09:00:00-00:00");
		Start start = new Start();
		start.setDateTime(dateTimeStart);
		start.setTimeZone("UTC");
		String dateTimeEnd = date.concat("T10:00:00-00:00");
		End end = new End();
		end.setDateTime(dateTimeEnd);
		end.setTimeZone("UTC");
		Event event = new Event();
		event.setStart(start);
		event.setEnd(end);
		event.setSummary(
				"Cita con el veterinario " + v.getVet().getFirstName() + " para la mascota " + v.getPet().getName());
		request.getSession().setAttribute("event", event);
		request.getSession().setAttribute("visit", v);
		model.addAttribute("event", event);
		model.addAttribute("visit", v);
		return view;
	}

	@PostMapping(value = "/visit/{visitId}/event/insert/")
	public String processInsertEvent(@PathVariable("visitId") int visitId, HttpServletRequest request,
			HttpServletResponse response, Event event,ModelMap model) throws ServletException, IOException {
		String accessToken = (String) request.getSession().getAttribute("accessToken");
		if (accessToken != null && !"".equals(accessToken)) {
			GoogleCalendarResources gogResource = new GoogleCalendarResources(accessToken);
			
			event = (Event) request.getSession().getAttribute("event");
		
			String calendarId = "primary";
			Event eventResponse = gogResource.insertEvent(event, calendarId);
			request.setAttribute("message",
					"Event " + eventResponse.getSummary() + " added to the primary calendar");
		}else {
			request.getRequestDispatcher("").forward(request, response);
		}

		return "redirect:/owner/pets/";
	}		

}
