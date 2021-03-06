
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {



	User findUserByUsername(String userName) throws DataAccessException;



}
