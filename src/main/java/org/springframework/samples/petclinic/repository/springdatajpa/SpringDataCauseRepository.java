
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.CauseRepository;

public interface SpringDataCauseRepository extends CauseRepository, Repository<Cause, Integer> {

	@Override
	@Query("select c from Cause c where c.id=:id")
	Cause findById(@Param("id") int id) throws DataAccessException;
 
	@Override
	@Query("select c from Cause c where c.status.id=2")
	Collection<Cause> findAcceptedCauses() throws DataAccessException;

	@Override
	@Query("select s from Status s where s.id=1")
	Status findPendingStatus() throws DataAccessException;

	@Override
	@Query("select c from Cause c where c.user.username LIKE :username")
	Collection<Cause> findMyCauses(String username) throws DataAccessException;

	@Override
	@Query("select c from Cause c where c.status.id=1")
	Collection<Cause> findPendingCauses() throws DataAccessException;

	@Override
	@Query("select s from Status s")
	Collection<Status> findStatus() throws DataAccessException;
	

}
