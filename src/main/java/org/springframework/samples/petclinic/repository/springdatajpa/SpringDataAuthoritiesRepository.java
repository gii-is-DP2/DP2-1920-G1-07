package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.CrudRepository;

import org.springframework.samples.petclinic.model.Authorities;

import org.springframework.samples.petclinic.repository.AuthoritiesRepository;

public interface SpringDataAuthoritiesRepository extends AuthoritiesRepository, CrudRepository<Authorities, String> {

}
