
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.stereotype.Component;

@Component
public class StatusFormatter implements Formatter<Status> {

	private final ReservationService reService;


	@Autowired
	public StatusFormatter(final ReservationService reService) {
		this.reService = reService;
	}

	@Override
	public String print(final Status statusName, final Locale locale) {
		return statusName.getName();
	}

	@Override
	public Status parse(final String text, final Locale locale) throws ParseException {
		Collection<Status> findAllStatus = this.reService.findAllStatus();
		for (Status s : findAllStatus) {
			if (s.getName().equals(text)) {
				return s;
			}
		}
		throw new ParseException("status not found: " + text, 0);

	}

}
