
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class DiagnosisServiceTests {

	@Autowired
	protected DiagnosisService	diagnosisService;

	@Autowired
	protected VetService		vetService;

	@Autowired
	protected VisitRepository	visitRepository;


	@Test
	void shouldFindDiagnosis() {
		Collection<Diagnosis> d = this.diagnosisService.findDiagnosis();
		Assertions.assertThat(d == null).isFalse();
		Assertions.assertThat(d.size()).isNotEqualTo(5);
	}

	@Test
	void shouldSaveDiagnosis() {
		Diagnosis diagnosis = new Diagnosis();
		int numDiagnosis = this.diagnosisService.findDiagnosis().size();
		Vet v = this.vetService.findVetById(7);
		Visit visit = this.visitRepository.findById(5);
		diagnosis.setDescription("todo bien");
		diagnosis.setDate(LocalDate.of(2013, 1, 10));
		diagnosis.setVet(v);
		diagnosis.setVisit(visit);
		diagnosis.setPet(visit.getPet());
		this.diagnosisService.saveDiagnosis(diagnosis);
		Assertions.assertThat(diagnosis.getId()).isNotNull();
		Assertions.assertThat(this.diagnosisService.findDiagnosis().size() == numDiagnosis + 1);
		Assertions.assertThat(this.diagnosisService.findDiagnosis().size() != numDiagnosis + 2);
	}

}
