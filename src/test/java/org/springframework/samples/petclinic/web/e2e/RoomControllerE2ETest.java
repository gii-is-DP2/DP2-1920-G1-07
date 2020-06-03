package org.springframework.samples.petclinic.web.e2e;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Collection;
import java.util.HashSet;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-mysql.properties")
class RoomControllerE2ETest {

	private static final int TEST_ROOM_ID = 1;
	private static final int TEST_ROOM2_ID = 2;
	private static final int TEST_ROOM_NO_HAVE_RESER_ID = 3;

	@Autowired
	private PetService petService;
	
	@Autowired
	private ReservationService resService;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testListadoRooms() throws Exception {
		mockMvc.perform(get("/rooms")).andExpect(view().name("rooms/roomList"))
				.andExpect(model().attributeExists("rooms"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
//	@Test
	void testListadoRoomsSitter() throws Exception {
		mockMvc.perform(get("sitter/rooms")).andExpect(view().name("rooms/roomList"))
				.andExpect(model().attributeExists("rooms"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testInitChangeSitter() throws Exception {
		mockMvc.perform(get("/rooms/{roomId}/sitter", TEST_ROOM_ID)).andExpect(view().name("rooms/selectSitter"))
				.andExpect(model().attributeExists("room", "sitters"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testProcessChangeSitter() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/sitter", TEST_ROOM_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/rooms/" + TEST_ROOM_ID));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/rooms/new")).andExpect(model().attributeExists("room")).andExpect(status().isOk())
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testProcessCreationForm() throws Exception {
		mockMvc.perform(post("/rooms/new").with(csrf()).param("id", "5").param("name", "Test Room")
				.param("capacity", "2").param("type", "dog")).andExpect(status().is3xxRedirection());
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/rooms/new").with(csrf()).param("id", "5").param("name", "Room1") // Aqui falla porque es
																								// un nombre duplicado,
																								// ya existe en la BBDD
				.param("capacity", "0")) // Idem ya que la capacidad no puede ser 0
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("room"))
				.andExpect(model().attributeHasFieldErrors("room", "name", "capacity"))
				.andExpect(model().attributeDoesNotExist("type")) // Falta por especificar el pet Type de la room
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testInitUpdateFormSuccess() throws Exception {
		mockMvc.perform(get("/rooms/{roomId}/edit", TEST_ROOM_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("room")).andExpect(view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "admin1", authorities = { "admin" })
	@Test
	void testProcessUpdateRoomFormSuccess() throws Exception {
		this.mockMvc
				.perform(post("/rooms/{roomId}/edit", TEST_ROOM_ID).with(csrf()).param("name", "Room Updated")
						.param("capacity", "3").param("type", "dog"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/rooms/" + TEST_ROOM_ID));
	}

	@WithMockUser(value = "admin1", authorities = { "admin" })
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacity() throws Exception {
		this.mockMvc
				.perform(post("/rooms/{roomId}/edit", TEST_ROOM_ID).with(csrf()).param("name", "Room1 Updated")
						.param("capacity", "0").param("type", "dog"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("room"))
				.andExpect(model().attributeHasFieldErrors("room", "capacity"))
				.andExpect(view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(username = "owner", authorities = { "owner" })
	@Test
	void testShowRoomsWithouthReservations() throws Exception {
		PetType cat = petService.findPetTypes().stream().filter(x -> x.getName().equals("cat")).findAny().get();
		this.mockMvc.perform(get("/rooms/{roomId}", TEST_ROOM_NO_HAVE_RESER_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room3"))))
				.andExpect(model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(3))))
				.andExpect(model().attribute("room", Matchers.hasProperty("type", Matchers.is(cat))))
				.andExpect(model().attribute("room", Matchers.hasProperty("reservations")))
				.andExpect(view().name("rooms/roomDetails"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testShowRoomsWithReservations() throws Exception {
		Collection<Reservation> res = new HashSet<Reservation>(); // Para verificar que esta room no tiene las reservas vacias.
		PetType dog = petService.findPetTypes().stream().filter(x -> x.getName().equals("dog")).findAny().get();
		this.mockMvc.perform(get("/rooms/{roomId}", TEST_ROOM2_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room2"))))
				.andExpect(model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(1))))
				.andExpect(model().attribute("room", Matchers.hasProperty("type", Matchers.is(dog))))
				.andExpect(model().attribute("room", Matchers.hasProperty("reservations",Matchers.not(res))))
				.andExpect(view().name("rooms/roomDetails"));
	}

	@WithMockUser(username = "owner", authorities = { "owner" })
	@Test
	void testShowRoomsWithReservationsAndOwner() throws Exception {
		Collection<Reservation> res = new HashSet<Reservation>(); // Para verificar que esta room no tiene las reservas
																	// vacias.
		PetType dog = this.petService.findPetTypes().stream().filter(x -> x.getName().equals("dog")).findAny().get();
		this.mockMvc.perform(get("/rooms/{roomId}", TEST_ROOM2_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room2"))))
				.andExpect(model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(1))))
				.andExpect(model().attribute("room", Matchers.hasProperty("type", Matchers.is(dog))))
				// Aqui se comprueba que el conjunto de reservas no esta vacio
				.andExpect(model().attribute("room", Matchers.hasProperty("reservations", Matchers.not(res))))
				// Si existe el atributo myReservations es que el Owner esta viendo sus propias
				// reservas
				.andExpect(model().attributeExists("myReservations")).andExpect(view().name("rooms/roomDetails"));
	}

	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteRoom() throws Exception {
		this.mockMvc.perform(get("/rooms/delete/{roomId}", TEST_ROOM_NO_HAVE_RESER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rooms/"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteRoomWithResercations() throws Exception {
		this.mockMvc
				.perform(get("/rooms/delete/{roomId}",TEST_ROOM2_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rooms/"));
	}
}
