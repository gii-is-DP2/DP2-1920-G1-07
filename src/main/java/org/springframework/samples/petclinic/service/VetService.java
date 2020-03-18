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

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VetService {

	@Autowired
	private VetRepository		vetRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;

	@Autowired
	private SpecialtyRepository	specialtyRepository;


	@Autowired
	public VetService(final VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Vet> findVets() throws DataAccessException {
		return this.vetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Vet findVetById(final int id) throws DataAccessException {
		return this.vetRepository.findById(id);
	}

	@Transactional
	public void saveVet(final Vet vet) throws DataAccessException {
		//creating vet
		this.vetRepository.save(vet);
		//creating user
		this.userService.saveUser(vet.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(vet.getUser().getUsername(), "veterinarian");
	}

	@Transactional(readOnly = true)
	public Collection<Specialty> findSpecialties() throws DataAccessException {
		return this.vetRepository.findSpecialties();
	}

	@Transactional(readOnly = true)
	public Collection<Visit> findVisits(final String userName) throws DataAccessException {
		return this.vetRepository.findVisits(userName);
	}
	@Transactional(readOnly = true)
	public Specialty findSpecialiesById(final int id) throws DataAccessException {
		Specialty s = this.specialtyRepository.findOneById(id);
		return s;
	}

	public Vet findVetByUserName(final String username) throws DataAccessException {
		Vet v = this.vetRepository.findVetByUserName(username);
		return v;
	}

	@Transactional(readOnly = true)
	public Collection<Diagnosis> findDiagnosis(final String userName) {
		return this.vetRepository.findDiagnosis(userName);
	}
}
