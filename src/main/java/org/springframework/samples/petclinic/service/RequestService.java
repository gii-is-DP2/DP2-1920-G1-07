
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

	private RequestRepository requestRepository;


	@Autowired
	public RequestService(final RequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}

	@Transactional
	public void saveRequest(final Request request) throws DataAccessException {
		this.requestRepository.save(request);
	}

	public Request findRequestByUser(final String username) {
		return this.requestRepository.findRequestByUser(username);
	}

	public Collection<Request> findAll() {
		return this.requestRepository.findAll();
	}

	public Request findRequestById(final int requestId) {
		return this.requestRepository.findRequestById(requestId);
	}

	public void deleteRequest(final Request request) {
		this.requestRepository.delete(request);

	}
}
