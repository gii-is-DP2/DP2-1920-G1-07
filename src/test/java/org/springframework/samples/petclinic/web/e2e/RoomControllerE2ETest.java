
package org.springframework.samples.petclinic.web.e2e;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
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
class RoomControllerE2ETest {

	private static final int	TEST_ROOM_ID				= 1;
	private static final int	TEST_ROOM2_ID				= 2;
	private static final int	TEST_ROOM_NO_HAVE_RESER_ID	= 3;

	@Autowired
	private PetService			petService;

	@Autowired
	private ReservationService	resService;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testListadoRooms() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms")).andExpect(MockMvcResultMatchers.view().name("rooms/roomList")).andExpect(MockMvcResultMatchers.model().attributeExists("rooms"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	//	@Test
	void testListadoRoomsSitter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("sitter/rooms")).andExpect(MockMvcResultMatchers.view().name("rooms/roomList")).andExpect(MockMvcResultMatchers.model().attributeExists("rooms"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitChangeSitter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/sitter", RoomControllerE2ETest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.view().name("rooms/selectSitter"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("room", "sitters"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessChangeSitter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/sitter", RoomControllerE2ETest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/" + RoomControllerE2ETest.TEST_ROOM_ID));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/new")).andExpect(MockMvcResultMatchers.model().attributeExists("room")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "5").param("name", "Test Room").param("capacity", "2").param("type", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "5").param("name", "Room1") // Aqui falla porque es
			// un nombre duplicado,
			// ya existe en la BBDD
			.param("capacity", "0")) // Idem ya que la capacidad no puede ser 0
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("room")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("room", "name", "capacity"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("type")) // Falta por especificar el pet Type de la room
			.andExpect(MockMvcResultMatchers.view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/edit", RoomControllerE2ETest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("room"))
			.andExpect(MockMvcResultMatchers.view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateRoomFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/edit", RoomControllerE2ETest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Room Updated").param("capacity", "3").param("type", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/" + RoomControllerE2ETest.TEST_ROOM_ID));
	}

	@WithMockUser(value = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacity() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/edit", RoomControllerE2ETest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Room1 Updated").param("capacity", "0").param("type", "dog"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("room")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("room", "capacity"))
			.andExpect(MockMvcResultMatchers.view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testShowRoomsWithouthReservations() throws Exception {
		PetType cat = this.petService.findPetTypes().stream().filter(x -> x.getName().equals("cat")).findAny().get();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerE2ETest.TEST_ROOM_NO_HAVE_RESER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room3")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type", Matchers.is(cat)))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("reservations")))
			.andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testShowRoomsWithReservations() throws Exception {
		Collection<Reservation> res = new HashSet<Reservation>(); // Para verificar que esta room no tiene las reservas vacias.
		PetType dog = this.petService.findPetTypes().stream().filter(x -> x.getName().equals("dog")).findAny().get();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerE2ETest.TEST_ROOM2_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room2")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(1))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type", Matchers.is(dog)))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("reservations", Matchers.not(res))))
			.andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(username = "owner", authorities = {
		"owner"
	})
	@Test
	void testShowRoomsWithReservationsAndOwner() throws Exception {
		Collection<Reservation> res = new HashSet<Reservation>(); // Para verificar que esta room no tiene las reservas
																	// vacias.
		PetType dog = this.petService.findPetTypes().stream().filter(x -> x.getName().equals("dog")).findAny().get();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerE2ETest.TEST_ROOM2_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room2")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(1))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type", Matchers.is(dog))))
			// Aqui se comprueba que el conjunto de reservas no esta vacio
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("reservations", Matchers.not(res))))
			// Si existe el atributo myReservations es que el Owner esta viendo sus propias
			// reservas
			.andExpect(MockMvcResultMatchers.model().attributeExists("myReservations")).andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteRoom() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete/{roomId}", RoomControllerE2ETest.TEST_ROOM_NO_HAVE_RESER_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteRoomWithResercations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete/{roomId}", RoomControllerE2ETest.TEST_ROOM2_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/"));
	}
}
