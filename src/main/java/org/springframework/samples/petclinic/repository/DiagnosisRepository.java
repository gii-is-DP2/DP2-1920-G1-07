
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;

public interface DiagnosisRepository {

	void save(Diagnosis diagnosis) throws DataAccessException;

	Collection<Diagnosis> findDiagnosis();

	Collection<Diagnosis> findMyDiagnosis(int petId);

}
