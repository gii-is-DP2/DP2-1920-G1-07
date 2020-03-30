
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Request;

public interface RequestRepository {

	Request findRequestByUser(String username) throws DataAccessException;

	void save(Request request) throws DataAccessException;

	Collection<Request> findAll() throws DataAccessException;

	Request findRequestById(int requestId) throws DataAccessException;

	void delete(Request request) throws DataAccessException;

}
