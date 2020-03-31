
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
		Cause cause = new Cause();
		cause.setTitle("");
		cause.setDescription("");
		cause.setMoney(10000.0);
		cause.setDeadline(LocalDate.of(2020, 11, 25));
		Status s = new Status();
		s.setName("PENDING");
		cause.setStatus(s);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cause>> constraintViolations = validator.validate(cause);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<Cause> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("title", "description");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

		//		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		//		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

	}

	@Test
	void shouldNotValidateMoney() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cause cause = new Cause();
		cause.setTitle("dsafs");
		cause.setDescription("dsafg");
		cause.setMoney(null);
		cause.setDeadline(LocalDate.of(2020, 11, 25));
		Status s = new Status();
		s.setName("PENDING");
		cause.setStatus(s);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cause>> constraintViolations = validator.validate(cause);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Cause> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("money");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

		//		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		//		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

	}

}
