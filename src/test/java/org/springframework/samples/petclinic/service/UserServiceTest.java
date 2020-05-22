package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;




import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserServiceTest {
	
	@Autowired
	protected UserService userService;
	
	/**
	 * author=Amine
	 * function = Test method findUserByUserName on User Service
	 */
	@Test
	void shouldFindUserByUserName() {
		User user = this.userService.findUserByUserName("amine");
		assertThat(user == null).isFalse();
		
		user = this.userService.findUserByUserName("fallo");
		assertThat(user == null).isTrue();
	}
}
