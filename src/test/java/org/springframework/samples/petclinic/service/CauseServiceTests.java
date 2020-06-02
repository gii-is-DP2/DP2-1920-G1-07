
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CauseServiceTests {

	@Autowired
	protected CauseService causeService;


	@Test
	void shouldFindAcceptedCauses() {
		Collection<Cause> causes = this.causeService.findAcceptedCauses();
		Integer tamaño = causes.size();

		Assertions.assertThat(tamaño).isEqualTo(2);
		Assertions.assertThat(tamaño).isNotEqualTo(4);
	}

	@Test
	void shouldFindPendingStatus() {
		//Arrange
		Status pending = this.causeService.findPendingStatus();
		//Act
		String name = pending.getName();
		//Assert
		Assertions.assertThat(name).isEqualTo("PENDING");
		Assertions.assertThat(name).isNotEqualTo("PENDINGG");
	}

	@Test
	void shoudFindMyCauses() {
		Collection<Cause> myCauses = this.causeService.findMyCauses("owner1");
		Integer tamaño = myCauses.size();

		Assertions.assertThat(tamaño).isEqualTo(3);
		Assertions.assertThat(tamaño).isNotEqualTo(2);
	}

	@Test
	void shouldFindPendingCauses() {
		Collection<Cause> causes = this.causeService.findPendingCauses();
		Integer tamaño = causes.size();

		Assertions.assertThat(tamaño).isEqualTo(1);
		Assertions.assertThat(tamaño).isNotEqualTo(3);
	}

	@Test
	void shouldFindStatus() {
		Collection<Status> status = this.causeService.findStatus();
		Integer tamaño = status.size();

		Assertions.assertThat(tamaño).isEqualTo(3);
		Assertions.assertThat(tamaño).isNotEqualTo(2);
	}

	@Test
	void shouldFindCauseById() {
		Cause cause = this.causeService.findCauseById(1);
		String title = cause.getTitle();

		Assertions.assertThat(title).isEqualTo("First Cause");
		Assertions.assertThat(title).isNotEqualTo("Second Cause");
	}

	@Test
	@Transactional
	void shouldInsertCause() {
		//Arrange
		Collection<Cause> causas1 = this.causeService.findAllCauses();
		Cause c = new Cause();
		c.setTitle("Causa prueba");
		c.setDescription("Esto es una causa de prueba");
		c.setId(100);
		c.setMoney(2000.0);
		c.setDeadline(LocalDate.of(2020, 10, 30));
		this.causeService.saveCauses(c);
		Collection<Cause> causas2 = this.causeService.findAllCauses();
		//Act
		Integer tamaño1 = causas1.size();
		Integer tamaño2 = causas2.size();
		//Assert
		Assertions.assertThat(tamaño2).isEqualTo(tamaño1 + 1);
		Assertions.assertThat(tamaño2).isNotEqualTo(tamaño1);
	}

}
