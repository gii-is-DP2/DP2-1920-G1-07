
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;

public interface SpringDataSpecialtyRepository extends SpecialtyRepository, CrudRepository<Specialty, Integer> {

	@Override
	@Query("SELECT e from Specialty e where e.id =:idSpecialty")
	Specialty findOne(int idSpecialty);
}
