package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(
		locations = "classpath:application-mysql.properties")
public class ReservationController2E2Test {

	private static final int TEST_RESERVATION_ID = 1;
	private static final int TEST_ROOM1_ID = 3;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(username = "owner", authorities = {"owner"})
	@Test
	void testInitNewReservationForm() throws Exception { 
		mockMvc.perform(get("/rooms/{roomId}/reservations/new",TEST_ROOM1_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("reservation","status"))
			   .andExpect(view().name("reservations/createReservationForm"));
	}
	
	@WithMockUser(username = "owner", authorities = {"owner"})
	@Test
	void testProcessNewReservationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM1_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/11")
				.param("exitDate","2020/07/15")
				.param("pet", "1")) //Cat
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	
	@WithMockUser(username = "owner", authorities = {"owner"})
	@Test
	void testProcessNewReservationFormHasErrorsOnDate() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM1_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/03/24")
				.param("exitDate", "2020/03/25")
				.param("pet", "1"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "entryDate"))
		.andExpect(model().attributeHasFieldErrors("reservation", "exitDate"))
		.andExpect(view().name("reservations/createReservationForm"));
	}
	@WithMockUser(username = "owner", authorities = {"owner"})
	@Test
	void testProcessNewReservationFormHasErrorsOnPet() throws Exception{
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM1_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/24")
				.param("exitDate", "2020/07/28")
				.param("pet", "2"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "pet"))
		.andExpect(view().name("reservations/createReservationForm"));
	
	}
	@WithMockUser(username = "owner", authorities = {"owner"})
	@Test
	void testProcessNewReservationFormHasErrorsPetNull() throws Exception{
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM1_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/24")
				.param("exitDate", "2020/07/28"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "pet"))
		.andExpect(view().name("reservations/createReservationForm"));
	
	}
	
	private static final int TEST_OWNER_ID = 12;
	private static final int TEST_ROOM10_ID = 10; //Es el id de una habitacion completa
	private static final int TEST_RESERVATION_ACCEPTED_ID = 6; //id de una reserva con estado Accepted
	
	@WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testInitProcessUpdateReservationFormToNotComletedRoom() throws Exception{
		mockMvc.perform(get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM1_ID,TEST_OWNER_ID,TEST_RESERVATION_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("editreservation","status"))
			   .andExpect(view().name("reservations/updateReservationForm"));
	}
	
	@WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testInitProcessUpdateReservationFormToAComletedRoom() throws Exception{
		mockMvc.perform(get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM10_ID,TEST_OWNER_ID,TEST_RESERVATION_ACCEPTED_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("editreservation","completedRoom","statusWithouthAccepted"))
			   .andExpect(view().name("reservations/updateReservationForm"));
	}
	@WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testProcessUpdateReservationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM1_ID,TEST_OWNER_ID,TEST_RESERVATION_ID)
					.with(csrf())
					.param("id", "1")
					.param("entryDate", "2021/07/19")
					.param("exitDate", "2021/07/29")
					.param("pet", "1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	@WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testProcessUpdateReservationFormErrorsOnDateAndPet() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM1_ID,TEST_OWNER_ID,TEST_RESERVATION_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/02/19")//Pasado debe petar
				.param("exitDate", "2020/02/29")//Pasado debe petar
				.param("pet", "2")) //dog mientras el type de Room es cat (debe petar)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "entryDate", "exitDate","pet"))
		.andExpect(view().name("reservations/updateReservationForm"));
	}
	
	@WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testProcessDeleteReservation() throws Exception {
	mockMvc.perform(get("/rooms/{roomId}/reservation/{reservationId}/delete",TEST_ROOM1_ID,TEST_RESERVATION_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	
		
	
	
}
