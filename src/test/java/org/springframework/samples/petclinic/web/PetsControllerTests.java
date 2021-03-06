package org.springframework.samples.petclinic.web;

/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.projections.OwnerPets;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetsController.class,
includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class PetsControllerTests {

	private static final int TEST_OWNER_ID = 1;
	private static final String TEST_USER_NAME = "spring";
	private static final int TEST_PET_ID = 1;
	private static final OwnerPets TEST_OWNERPETS = new OwnerPets() {
		
		@Override
		public String getVisitDescription() {
			return "Hola";
		}
		
		@Override
		public LocalDate getVisitDate() {
			LocalDate a = LocalDate.of(2015, 5, 6);
			return a;
		}
		
		@Override
		public String getType() {
			return "Cat";
		}
		
		@Override
		public String getName() {
			return "Jule";
		}
		
		@Override
		public Integer getId() {
			return 20;
		}
		
		@Override
		public LocalDate getBirthDate() {
			LocalDate a = LocalDate.of(2010, 5, 6);
			return a;
		}
	};
	
private static final OwnerPets TEST_OWNERPETS2 = new OwnerPets() {
		
		@Override
		public String getVisitDescription() {
			return "Hola2";
		}
		
		@Override
		public LocalDate getVisitDate() {
			LocalDate a = LocalDate.of(2015, 5, 8);
			return a;
		}
		
		@Override
		public String getType() {
			return "Cat";
		}
		
		@Override
		public String getName() {
			return "Jule";
		}
		
		@Override
		public Integer getId() {
			return 20;
		}
		
		@Override
		public LocalDate getBirthDate() {
			LocalDate a = LocalDate.of(2010, 5, 6);
			return a;
		}
	};
	
private static final OwnerPets TEST_OWNERPETS3 = new OwnerPets() {
		
		@Override
		public String getVisitDescription() {
			return null;
		}
		
		@Override
		public LocalDate getVisitDate() {
			return null;
		}
		
		@Override
		public String getType() {
			return "Cat";
		}
		
		@Override
		public String getName() {
			return "Jula";
		}
		
		@Override
		public Integer getId() {
			return 25;
		}
		
		@Override
		public LocalDate getBirthDate() {
			LocalDate a = LocalDate.of(2010, 5, 7);
			return a;
		}
	};
	@Autowired
	private PetsController petsController;


	@MockBean
	private PetService petService;

	@MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		Pet pet = new Pet();
		pet.setType(cat);
		pet.setBirthDate(LocalDate.now());
		pet.setName("Pet");
		Collection<OwnerPets> owpet = new ArrayList<OwnerPets>();
		owpet.add(TEST_OWNERPETS);
		owpet.add(TEST_OWNERPETS2);
		owpet.add(TEST_OWNERPETS3);
		given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(new Owner());
		given(this.ownerService.findOwnerByUser(TEST_USER_NAME)).willReturn(new Owner());
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.petService.findOwnerPetsById(TEST_USER_NAME)).willReturn(owpet);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owner/pets/new", TEST_OWNER_ID)).andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owner/pets/new", TEST_OWNER_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owner/pets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owner/pets/new", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("birthDate", "2015/02/12"))
		.andExpect(model().attributeHasErrors("pet"))
		.andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormDateFuture() throws Exception {
		mockMvc.perform(post("/owner/pets/new", TEST_OWNER_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2021/02/12"))
		.andExpect(model().attributeHasErrors("pet"))
		.andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owner/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
		.andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/owner/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owner/pets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owner/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("name", "Betty")
			.param("birthDate", "2015/02/12"))
		.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessShow() throws Exception {
		mockMvc.perform(get("/owner/pets", TEST_OWNER_ID, TEST_PET_ID))
		.andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
		.andExpect(view().name("pets/petList"));
	}

}
