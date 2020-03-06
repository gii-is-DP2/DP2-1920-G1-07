
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.stereotype.Service;

@Service
public class CauseService {

	@Autowired
	private CauseRepository causeRepository;


	@Autowired
	public CauseService(final CauseRepository causeRepository) {
		this.causeRepository = causeRepository;
	}

	public void saveCauses(@Valid final Cause cause) throws DataAccessException {
		this.causeRepository.save(cause);
	}

	@Transactional
	public Collection<Cause> findAcceptedCauses() throws DataAccessException {
		return this.causeRepository.findAcceptedCauses();
	}

	@Transactional
	public Status findPendingStatus() throws DataAccessException {
		return this.causeRepository.findPendingStatus();
	}

	public Collection<Cause> findMyCauses(final String userName) throws DataAccessException {
		return this.causeRepository.findMyCauses(userName);
	}

}
