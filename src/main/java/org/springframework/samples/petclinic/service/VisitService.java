
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	@Autowired
	private VisitRepository visitRepo;


	@Transactional
	public Iterable<Visit> visitFind() {
		return this.visitRepo.findAll();
	}

	public Visit findById(final int visitId) {
		return this.visitRepo.findById(visitId);

	}
}
