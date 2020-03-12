
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Visits {

	private List<Visit> visits;


	@XmlElement
	public List<Visit> getVisitList() {
		if (this.visits == null) {
			this.visits = new ArrayList<>();
		}
		return this.visits;
	}
}
