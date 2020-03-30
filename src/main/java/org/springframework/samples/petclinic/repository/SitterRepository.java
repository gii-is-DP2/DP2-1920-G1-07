
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Sitter;

public interface SitterRepository extends CrudRepository<Sitter, Integer> {

}
