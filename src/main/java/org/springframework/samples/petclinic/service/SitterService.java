
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Sitter;
import org.springframework.samples.petclinic.repository.SitterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SitterService {

	private SitterRepository sitterRepository;


	public SitterService(final SitterRepository sitterRepository) {
		this.sitterRepository = sitterRepository;
	}

	@Transactional
	public void saveSitter(final Sitter sitter) throws DataAccessException {
		this.sitterRepository.save(sitter);
	}

	public Collection<Sitter> findAll() {
		Collection<Sitter> sitters = new ArrayList<Sitter>();
		Iterable<Sitter> sit = this.sitterRepository.findAll();
		sit.forEach(sitters::add);
		return sitters;
	}

	public Sitter findById(final int i) {
		return this.sitterRepository.findById(i).get();
	}

}
