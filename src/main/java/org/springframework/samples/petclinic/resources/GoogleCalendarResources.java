package org.springframework.samples.petclinic.resources;

import java.util.logging.Logger;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.springframework.samples.petclinic.model.googleCalendar.Event;

public class GoogleCalendarResources {
	
	private static final Logger log = Logger.getLogger(GoogleCalendarResources.class.getName());
	
	private final String access_token;
	private final String URL_INSERT_EVENT = "https://www.googleapis.com/calendar/v3/calendars/%calendarId/events";
	
	public GoogleCalendarResources(String access_token) {
		this.access_token = access_token;
	}
	
	public GoogleCalendarResources() {
		this.access_token = null;
	}
	
	public Event insertEvent(Event eventResponse,String calendarId) {
		ClientResource cr = null;
		Event event = null;
		
		try {
			cr = new ClientResource(URL_INSERT_EVENT.replace("%calendarId", calendarId));
			
			ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_OAUTH_BEARER);
			challengeResponse.setRawValue(access_token);
			cr.setChallengeResponse(challengeResponse);
			
			event = cr.post(eventResponse,Event.class);
		}catch (ResourceException e) {
			
			log.warning("Error when inserting event "+cr.getResponse().getStatus());
		}
		return event;
		
	}
	
}
