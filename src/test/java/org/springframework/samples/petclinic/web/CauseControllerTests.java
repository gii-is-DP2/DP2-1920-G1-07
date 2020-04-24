
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

	private static final int	TEST_CAUSE_ID			= 1;
	private static final int	TEST_PENDING_STATUS_ID	= 1;

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
		p.setId(CauseControllerTests.TEST_PENDING_STATUS_ID);
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
			.param("status.name", "PENDING")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/cause"));
	}

	//Deadline pasado
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsOnDate() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "10000.0").param("deadline", "2019/07/25"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "deadline"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	//Dinero negativo
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsOnMoney() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Test Title").param("description", "Test Description").param("money", "-10000.0").param("deadline", "2019/07/25"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "money"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	//Form vacío
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cause/new").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause"))
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cause/myCauses/owner1")).andExpect(MockMvcResultMatchers.status().isOk())
			//		.andExpect(MockMvcResultMatchers.model().attributeExists("causes"))
			.andExpect(MockMvcResultMatchers.view().name("causes/myCausesList"));
	}

	//Pruebas para la vista de causas pendientes
	@WithMockUser(value = "spring")
	@Test
	void testFormPendingCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/PendingCauses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/pendingCauses"));
	}

	//Prueba para iniciar form de actualizar causas pendientes
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateStatusForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/PendingCauses/cause/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
			.andExpect(MockMvcResultMatchers.view().name("causes/updatePendingCauseForm"));
	}

	//Prueba post form para actualizar causa
	@WithMockUser(value = "spring")
	@Test
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/PendingCauses/cause/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("status.name", "ACCEPTED"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/causes/PendingCauses"));
	}

}
