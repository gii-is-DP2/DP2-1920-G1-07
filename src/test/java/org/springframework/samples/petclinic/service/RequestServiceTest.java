
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RequestServiceTest {

	@Autowired
	protected OwnerService		ownerService;
	@Autowired
	protected RequestService	requestService;


	@Test
	@Transactional
	void shouldSaveRequest() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		int numRequest = this.requestService.findAll().size();
		Request request = new Request();
		request.setUser(owner6.getUser());
		request.setAddress("Calle A");
		request.setTelephone("666666666");

		this.requestService.saveRequest(request);
		assertThat(request.getId()).isNotNull();
		assertThat(this.requestService.findAll().size()==(numRequest+1));
	}

	@Test
	@Transactional
	void shouldThrowErrorSavingRequest() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Request request = new Request();
		request.setUser(owner6.getUser());

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.requestService.saveRequest(request);
		});
	}
	
	@Test
	@Transactional
	void shouldDeleteRequest() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		int numRequest = this.requestService.findAll().size();
		Request request = new Request();
		request.setUser(owner6.getUser());
		request.setAddress("Calle A");
		request.setTelephone("666666666");

		this.requestService.saveRequest(request);
		assertThat(this.requestService.findAll().size()==(numRequest+1));
		this.requestService.deleteRequest(request);
		assertThat(this.requestService.findAll().size()==numRequest);
	}
	
//	@Test
	@Transactional
	void shouldFindRequestByUser() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Request request = new Request();
		request.setUser(owner6.getUser());
		request.setAddress("Calle A");
		request.setTelephone("666666666");

		this.requestService.saveRequest(request);
		Request requestFound = this.requestService.findRequestByUser(owner6.getUser().getUsername());
		assertThat(requestFound.equals(request));
	}
}
