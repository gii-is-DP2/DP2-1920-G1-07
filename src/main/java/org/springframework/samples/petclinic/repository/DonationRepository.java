package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;

import org.springframework.samples.petclinic.model.Donation;

public interface DonationRepository {
	
	Collection<Donation> findMyDonationCause(int idCause) throws DataAccessException;
	
	void save(Donation donation) throws DataAccessException;
	
	void delete(Donation donation) throws DataAccessException;
	
	Optional<Donation> findById(int id) throws DataAccessException;
	
	Iterable<Donation> findAll() throws DataAccessException;
	
	Collection<Donation> findMyDonations(String username) throws DataAccessException;
	
	Donation findDonationById(int id) throws DataAccessException;
}