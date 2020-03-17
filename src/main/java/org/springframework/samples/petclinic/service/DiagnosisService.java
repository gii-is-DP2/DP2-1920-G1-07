
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiagnosisService {

	@Autowired
	private DiagnosisRepository diagnosisRepository;


	@Autowired
	public DiagnosisService(final DiagnosisRepository diagnosisRepository) {
		this.diagnosisRepository = diagnosisRepository;
	}

	@Transactional
	public void saveDiagnosis(final Diagnosis diagnosis) throws DataAccessException {
		this.diagnosisRepository.save(diagnosis);

	}

	public Collection<Diagnosis> findDiagnosis() {
		// TODO Auto-generated method stub
		return this.diagnosisRepository.findDiagnosis();
	}

	public Collection<Diagnosis> findMyDiagnosis(final String userName) {
		// TODO Auto-generated method stub
		return this.diagnosisRepository.findMyDiagnosis(userName);
	}

}
