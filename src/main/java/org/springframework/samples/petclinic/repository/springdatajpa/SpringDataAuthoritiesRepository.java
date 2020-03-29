package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.repository.AuthoritiesRepository;

public interface SpringDataAuthoritiesRepository extends AuthoritiesRepository, Repository<Authorities, String> {

}
