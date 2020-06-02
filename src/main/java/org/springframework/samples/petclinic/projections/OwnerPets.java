
package org.springframework.samples.petclinic.projections;

import java.time.LocalDate;

public interface OwnerPets {

	String getName();
	LocalDate getBirthDate();
	String getType();
	Integer getId();
	LocalDate getVisitDate();
	String getVisitDescription();
}
