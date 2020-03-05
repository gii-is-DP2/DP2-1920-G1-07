package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
	
	@Autowired
	private DonationRepository donationRepository;
	
	
	@Transactional
	public int donationCount() {
		return (int) donationRepository.count();
	}
	
	@Transactional
	public Iterable<Donation> findAllDonations(){
		
		return donationRepository.findAll();
	}
	
	
	

	@Transactional
	public void saveDonation(Donation donation){
		donationRepository.save(donation);
	}

	@Transactional
	public void delete(Donation donation) {
		 donationRepository.delete(donation);
		
	}

	@Transactional(readOnly=true)
	public Optional<Donation> findDonationById(int donationId) {
		
		return donationRepository.findById(donationId);
	}
}
