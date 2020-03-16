
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {

//	User findUserByName(String name) throws DataAccessException;

	User findUserByUsername(String userName) throws DataAccessException;

//	void save(User user) throws DataAccessException;

}
