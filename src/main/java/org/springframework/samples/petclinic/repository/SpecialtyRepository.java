
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository {

	Specialty findOne(int idSpecialty) throws DataAccessException;
}
