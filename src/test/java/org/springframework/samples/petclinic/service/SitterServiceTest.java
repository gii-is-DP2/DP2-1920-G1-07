package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.model.Sitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SitterServiceTest {

	@Autowired
	protected OwnerService		ownerService;
	@Autowired
	protected SitterService	sitterService;
	
	@Test
	@Transactional
	void shouldSaveSitter() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Sitter sitter = new Sitter();
		sitter.setFirstName("Ejemplo");
		sitter.setAddress("Calle k");
		sitter.setLastName("Ej");
		sitter.setTelephone("666666666");
		sitter.setUser(owner6.getUser());

		this.sitterService.saveSitter(sitter);
		assertThat(sitter.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldThrowErrorSavingSitter() {
		Sitter sitter = new Sitter();
		sitter.setFirstName("Ejemplo");
		sitter.setLastName("Ej");

		
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.sitterService.saveSitter(sitter);
		});
	}
}
