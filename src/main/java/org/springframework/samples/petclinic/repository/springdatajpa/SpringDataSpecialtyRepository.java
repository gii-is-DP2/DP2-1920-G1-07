
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;

public interface SpringDataSpecialtyRepository extends SpecialtyRepository, Repository<Specialty, Integer> {

	@Override
	@Query("SELECT e FROM Specialty e WHERE e.id = :idSpecialty")
	Specialty findOneById(int idSpecialty);

}
