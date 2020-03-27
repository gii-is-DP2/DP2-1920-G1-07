package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;

public interface SpringDataDonationRepository extends DonationRepository, Repository<Donation, Integer>  {

	@Override
	@Query("select d from Donation d where d.causes.id=:idCause")
	public Collection<Donation> findMyDonationCause(@Param(value="idCause")int idCause);
	
	@Override
	@Query("select d from Donation d where d.user.username LIKE :username")
	public Collection<Donation> findMyDonations(String username) throws DataAccessException;
	

}
