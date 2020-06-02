
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;


public interface SpringDataUserRepository extends UserRepository, Repository<User, String> {


	@Override
	@Query("select u from User u where u.username='username'")
	User findUserByUsername(String userName);

}
