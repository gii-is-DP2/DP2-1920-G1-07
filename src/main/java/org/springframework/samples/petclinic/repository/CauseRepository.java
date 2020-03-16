
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Status;

public interface CauseRepository {

	/**
	 * Save an <code>Cause</code> to the data store, either inserting or updating it.
	 *
	 * @param causes
	 *            the <code>Causes</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Cause cause) throws DataAccessException;

	/**
	 * Retrieve an <code>Cause</code> from the data store by id.
	 *
	 * @param id
	 *            the id to search for
	 * @return the <code>Cause</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException
	 *             if not found
	 */
	Cause findById(int id) throws DataAccessException;

	/**
	 * Retrieve all <code>Cause</code>s from the data store.
	 *
	 * @return a <code>Collection</code> of <code>Cause</code>s
	 */
	Collection<Cause> findAcceptedCauses() throws DataAccessException;

	Status findPendingStatus() throws DataAccessException;

	Collection<Cause> findMyCauses(String userName) throws DataAccessException;

	Collection<Cause> findPendingCauses() throws DataAccessException;

	Collection<Status> findStatus() throws DataAccessException;
}
