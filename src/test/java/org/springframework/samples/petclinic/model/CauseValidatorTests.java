
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class CauseValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidate() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		String title = "Test title";
		String description = "Test Description";
		LocalDate deadline = LocalDate.of(2020, 11, 25);
		Status s = new Status();
		s.setName("PENDING");

		//Title vacía
		Cause cause1 = new Cause();
		cause1.setTitle("");
		cause1.setDescription(description);
		cause1.setMoney(10000.0);
		cause1.setDeadline(deadline);
		cause1.setStatus(s);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cause>> constraintViolations = validator.validate(cause1);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

		//Money null
		Cause cause2 = new Cause();
		cause2.setTitle(title);
		cause2.setDescription(description);
		cause2.setMoney(null);
		cause2.setDeadline(deadline);
		cause2.setStatus(s);

		Set<ConstraintViolation<Cause>> constraintViolations2 = validator.validate(cause2);
		Assertions.assertThat(constraintViolations2.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation2 = constraintViolations2.iterator().next();
		Assertions.assertThat(violation2.getPropertyPath().toString()).isEqualTo("money");
		Assertions.assertThat(violation2.getMessage()).isEqualTo("must not be null");

		//Money negativo
		Cause cause3 = new Cause();
		cause3.setTitle(title);
		cause3.setDescription(description);
		cause3.setMoney(-1000.0);
		cause3.setDeadline(deadline);
		cause3.setStatus(s);

		Set<ConstraintViolation<Cause>> constraintViolations3 = validator.validate(cause3);
		Assertions.assertThat(constraintViolations3.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation3 = constraintViolations3.iterator().next();
		Assertions.assertThat(violation3.getPropertyPath().toString()).isEqualTo("money");
		Assertions.assertThat(violation3.getMessage()).isEqualTo("must be greater than or equal to 0.01");

		//Deadline pasado
		Cause cause4 = new Cause();
		cause4.setTitle(title);
		cause4.setDescription(description);
		cause4.setMoney(10000.0);
		cause4.setDeadline(LocalDate.of(2019, 11, 25));
		cause4.setStatus(s);

		Set<ConstraintViolation<Cause>> constraintViolations4 = validator.validate(cause4);
		Assertions.assertThat(constraintViolations4.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation4 = constraintViolations4.iterator().next();
		Assertions.assertThat(violation4.getPropertyPath().toString()).isEqualTo("deadline");
		Assertions.assertThat(violation4.getMessage()).isEqualTo("must be a future date");

		//Description vacía
		Cause cause5 = new Cause();
		cause5.setTitle(title);
		cause5.setDescription("");
		cause5.setMoney(10000.0);
		cause5.setDeadline(deadline);
		cause5.setStatus(s);

		Validator validator5 = this.createValidator();
		Set<ConstraintViolation<Cause>> constraintViolations5 = validator5.validate(cause5);

		Assertions.assertThat(constraintViolations5.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation5 = constraintViolations5.iterator().next();
		Assertions.assertThat(violation5.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(violation5.getMessage()).isEqualTo("must not be empty");

	}

}
