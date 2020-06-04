
package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Request;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RequestService;
import org.springframework.samples.petclinic.service.SitterService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = RequestController.class,
includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class RequestControllerTests {

	private static final int	TEST_OWNER_ID	= 1;
	private static final String	TEST_USER_NAME	= "spring";
	private static final int	TEST_REQUEST_ID	= 1;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private PetService			petService;

	@MockBean
	private RequestService		requestService;

	@MockBean
	private UserService			userService;

	@MockBean
	private SitterService		sitterService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		User res = new User();
		res.setUsername("spring");
		Request req = new Request();
		req.setUser(res);
		req.setAddress("Calle k");
		req.setTelephone("666666666");
		req.setType(cat);
		Owner ow = new Owner();
		ow.setUser(res);
		ow.setFirstName("Hola");
		ow.setLastName("Adios");
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.requestService.findAll()).willReturn(Lists.newArrayList(req));
		BDDMockito.given(this.ownerService.findOwnerById(RequestControllerTests.TEST_OWNER_ID)).willReturn(new Owner());
		BDDMockito.given(this.ownerService.findOwnerByUser(RequestControllerTests.TEST_USER_NAME)).willReturn(ow);
		BDDMockito.given(this.userService.findUserByUserName(RequestControllerTests.TEST_USER_NAME)).willReturn(res);
		BDDMockito.given(this.requestService.findRequestById(RequestControllerTests.TEST_REQUEST_ID)).willReturn(req);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/request/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/requestForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("request"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormSuccess() throws Exception {
	mockMvc.perform(post("/request/new", TEST_REQUEST_ID)
						.with(csrf())
						.param("address", "Calle k")
						.param("telephone", "666666666")
						.param("type", "hamster"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
}
	
	@WithMockUser(value = "spring")
    @Test
    void testDuplicatedProcessCreationFormSuccess() throws Exception {
	BDDMockito.given(this.requestService.findRequestByUser(RequestControllerTests.TEST_USER_NAME)).willReturn(new Request());
	mockMvc.perform(post("/request/new")
						.with(csrf())
						.param("address", "Calle k")
						.param("telephone", "666666666")
						.param("type", "hamster"))
			.andExpect(status().isOk())
			.andExpect(view().name("request/requestForm"));
}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormWithErrors() throws Exception {
	mockMvc.perform(post("/request/new")
						.with(csrf())
						.param("address", "Calle k"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("request"))
			.andExpect(view().name("request/requestForm"));
}
	@WithMockUser(value = "spring")
	@Test
	void testShow() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("request/requestList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("requests"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testReject() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/reject", TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testAccept() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/request/{requestId}/accept", TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/request"));
	}
}
