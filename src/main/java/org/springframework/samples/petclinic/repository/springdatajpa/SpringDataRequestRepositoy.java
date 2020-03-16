
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.repository.RequestRepository;

public interface SpringDataRequestRepositoy extends RequestRepository, Repository<Request, Integer> {

	@Override
	@Query("SELECT u from Request u where u.user.username =:name")
	Request findRequestByUser(@Param("name") String name);

	@Override
	@Query("SELECT u from Request u")
	Collection<Request> findAll();

	@Override
	@Query("SELECT u from Request u where u.id =:id")
	Request findRequestById(@Param("id") int id);

}
