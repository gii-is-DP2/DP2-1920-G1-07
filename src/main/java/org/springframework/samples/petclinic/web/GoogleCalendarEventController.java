package org.springframework.samples.petclinic.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class GoogleCalendarEventController {
	
	@Autowired
	private VisitService visitService;
	
	private GoogleCalendarResources googResources;
	
	@GetMapping(value="/googleCalendar/events/list")
	public String listOfTheTenNextEvents(HttpServletRequest request) {
		String view="";
		String accessToken = (String) request.getSession().getAttribute("accessToken");
		
		if(accessToken != null) {
			
		}
		
		
		return view;
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
