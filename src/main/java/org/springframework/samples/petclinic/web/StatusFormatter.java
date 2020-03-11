
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.stereotype.Component;

@Component
public class StatusFormatter implements Formatter<Status> {

	private final CauseService causeService;


	@Autowired
	public StatusFormatter(final CauseService causeService) {
		this.causeService = causeService;
	}

	@Override
	public String print(final Status status, final Locale locale) {
		return status.getName();
	}

	@Override
	public Status parse(final String text, final Locale locale) throws ParseException {
		Collection<Status> findStatus = this.causeService.findStatus();
		for (Status s : findStatus) {
			if (s.getName().equals(text)) {
				return s;
			}
		}
		throw new ParseException("status not found: " + text, 0);

	}

}
