package org.springframework.samples.petclinic.service;

import java.util.Collection;

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
}
