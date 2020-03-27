
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CauseController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CauseControllerTests {

	private static final int	TEST_CAUSE_ID	= 1;

	@Autowired
	private CauseController		causeController;

	@MockBean
	private CauseService		causeService;

	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc				mockMvc;

	private Cause				c;


	@BeforeEach
	void setup() {
		User prueba = this.userService.findUserByUserName("owner1");
		this.c = new Cause();
		this.c.setId(CauseControllerTests.TEST_CAUSE_ID);
		this.c.setTitle("Test cause");
		this.c.setDescription("This is a test cause");
		this.c.setDeadline(LocalDate.of(2020, 11, 25));
		this.c.setMoney(10000.0);
		Status p = new Status();
		p.setName("PENDING");
		this.c.setStatus(p);
		this.c.setUser(prueba);
		BDDMockito.given(this.causeService.findCauseById(CauseControllerTests.TEST_CAUSE_ID)).willReturn(this.c);
	}

	//Prueba obtener el form para crear causa
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	//Prueba post form para crear causa
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "10000.0").param("deadline", "2020/07/25")
			.param("status.name", "PENDING")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	//No funciona
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("description", "cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("money", "cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	//Pruebas para la vista de causas aceptadas
	@WithMockUser(value = "spring")
	@Test
	void testInitFindFormCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause")).andExpect(MockMvcResultMatchers.view().name("causes/causesList"));
	}

	//Pruebas para la vista ver mis causas
	@WithMockUser(value = "spring")
	@Test
	void testFormMyCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/myCauses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("causes"))
			.andExpect(MockMvcResultMatchers.view().name("causes/myCausesList"));
	}

	//Pruebas para la vista de causas pendientes
	@WithMockUser(value = "spring")
	@Test
	void testFormPendingCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/PendingCauses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/pendingCauses"));
	}

	//No funciona
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitUpdateStatusForm() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/PendingCauses/cause/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID))
	//		.andExpect(MockMvcResultMatchers.status().isOk())
	//		.andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
	//			.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("title", Matchers.is("Test Causa"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("description", Matchers.is("This is a test causa"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("status.name", Matchers.is("PENDING"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("money", Matchers.is("1000.0"))))
	//			.andExpect(MockMvcResultMatchers.view().name("causes/updatePendingCauseForm"));
	//	}

}
