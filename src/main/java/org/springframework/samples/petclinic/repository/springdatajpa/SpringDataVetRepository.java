/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VetRepository;

/**
 * Spring Data JPA specialization of the {@link VetRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataVetRepository extends VetRepository, Repository<Vet, Integer> {

	@Override
	@Query("select s from Specialty s order by s.name")
	List<Specialty> findSpecialties() throws DataAccessException;



	@Override
	@Query("select v from Visit v where v.vet.user.username = :username")
	List<Visit> findVisits(String username) throws DataAccessException;

	@Override
	@Query("select d from Diagnosis d where d.vet.user.username = :username")
	List<Diagnosis> findDiagnosis(String username) throws DataAccessException;

	@Override
	@Query("select v from Vet v where v.user.username =:username")
	Vet findVetByUserName(String username) throws DataAccessException;

	@Override
	@Modifying
	@Query(value = "INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (:id_vet, :id_specialty)", nativeQuery = true)
	void saveVetSpecialty(@Param("id_vet") int idVet, @Param("id_specialty") int idSpecialty) throws DataAccessException;
}
