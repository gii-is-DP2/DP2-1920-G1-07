
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;

public interface SpringDataDiagnosisRepository extends DiagnosisRepository, Repository<Diagnosis, Integer> {

	@Override
	@Query("select d from Diagnosis d")
	Collection<Diagnosis> findDiagnosis() throws DataAccessException;

	@Override
	@Query("select d from Diagnosis d where d.user.username LIKE :username")
	Collection<Diagnosis> findMyDiagnosis(String username) throws DataAccessException;
}
