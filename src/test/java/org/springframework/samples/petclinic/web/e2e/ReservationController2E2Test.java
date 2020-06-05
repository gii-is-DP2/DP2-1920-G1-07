
package org.springframework.samples.petclinic.web.e2e;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(locations = "classpath:application-mysql.properties")
class ReservationController2E2Test {

	private static final int	TEST_RESERVATION_ID	= 1;
	private static final int	TEST_ROOM1_ID		= 3;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testInitNewReservationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/reservations/new", ReservationController2E2Test.TEST_ROOM1_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("reservation", "status")).andExpect(MockMvcResultMatchers.view().name("reservations/createReservationForm"));
	}

	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testProcessNewReservationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/reservations/new", ReservationController2E2Test.TEST_ROOM1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2020/07/11")
				.param("exitDate", "2020/07/15").param("pet", "1")) //Cat
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/{roomId}"));
	}

	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testProcessNewReservationFormHasErrorsOnDate() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/reservations/new", ReservationController2E2Test.TEST_ROOM1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2020/03/24")
				.param("exitDate", "2020/03/25").param("pet", "1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("reservation")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reservation", "entryDate"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reservation", "exitDate")).andExpect(MockMvcResultMatchers.view().name("reservations/createReservationForm"));
	}
	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testProcessNewReservationFormHasErrorsOnPet() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/reservations/new", ReservationController2E2Test.TEST_ROOM1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2020/07/24")
				.param("exitDate", "2020/07/28").param("pet", "2"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("reservation")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reservation", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("reservations/createReservationForm"));

	}
	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testProcessNewReservationFormHasErrorsPetNull() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/rooms/{roomId}/reservations/new", ReservationController2E2Test.TEST_ROOM1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2020/07/24").param("exitDate", "2020/07/28"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("reservation")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reservation", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("reservations/createReservationForm"));

	}


	private static final int	TEST_OWNER_ID					= 12;
	private static final int	TEST_ROOM10_ID					= 10; //Es el id de una habitacion completa
	private static final int	TEST_RESERVATION_ACCEPTED_ID	= 6; //id de una reserva con estado Accepted


	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitProcessUpdateReservationFormToNotComletedRoom() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit", ReservationController2E2Test.TEST_ROOM1_ID, ReservationController2E2Test.TEST_OWNER_ID, ReservationController2E2Test.TEST_RESERVATION_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("editreservation", "status")).andExpect(MockMvcResultMatchers.view().name("reservations/updateReservationForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitProcessUpdateReservationFormToAComletedRoom() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit", ReservationController2E2Test.TEST_ROOM10_ID, ReservationController2E2Test.TEST_OWNER_ID, ReservationController2E2Test.TEST_RESERVATION_ACCEPTED_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("editreservation", "completedRoom", "statusWithouthAccepted"))
			.andExpect(MockMvcResultMatchers.view().name("reservations/updateReservationForm"));
	}
	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateReservationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit", ReservationController2E2Test.TEST_ROOM1_ID, ReservationController2E2Test.TEST_OWNER_ID, ReservationController2E2Test.TEST_RESERVATION_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2021/07/19").param("exitDate", "2021/07/29").param("pet", "1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/{roomId}"));
	}
	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateReservationFormErrorsOnDateAndPet() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit", ReservationController2E2Test.TEST_ROOM1_ID, ReservationController2E2Test.TEST_OWNER_ID, ReservationController2E2Test.TEST_RESERVATION_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("entryDate", "2020/02/19")//Pasado debe petar
				.param("exitDate", "2020/02/29")//Pasado debe petar
				.param("pet", "2")) //dog mientras el type de Room es cat (debe petar)
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reservation")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reservation", "entryDate", "exitDate", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("reservations/updateReservationForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessDeleteReservation() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/reservation/{reservationId}/delete", ReservationController2E2Test.TEST_ROOM1_ID, ReservationController2E2Test.TEST_RESERVATION_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/{roomId}"));
	}

}
