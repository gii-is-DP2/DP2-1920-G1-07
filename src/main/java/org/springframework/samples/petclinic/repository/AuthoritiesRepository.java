package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	

}
