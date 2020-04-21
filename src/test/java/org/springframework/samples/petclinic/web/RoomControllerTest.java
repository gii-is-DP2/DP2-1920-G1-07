package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.Arrays;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
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
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;

import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;

import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.samples.petclinic.service.SitterService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import java.awt.print.Printable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = RoomController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class RoomControllerTest {

	public static final int		TEST_ROOM_ID					= 1;
	public static final int		TEST_OWNER_ID					= 2;
	private static final int	TEST_SPRING_ID					= 1;
	public static final int		TEST_ROOM_WITHRESERVATION_ID	= 2;
	public static final int		TEST_RESERVATION_ID				= 1;

	@Autowired
	private RoomController		roomController;

	@MockBean
	private RoomService			roomService;
	@MockBean
	private ReservationService	reservationService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private PetService			petService;

	@MockBean
	private SitterService		sitterService;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "spring")
	@BeforeEach
	void setup() {
		PetType dogType = new PetType();
		dogType.setId(2);
		dogType.setName("dog");

		Room room = new Room();
		room.setId(RoomControllerTest.TEST_ROOM_ID);
		room.setCapacity(4);
		room.setName("Test Room");
		room.setType(dogType);

		User user = new User();
		user.setUsername("spring");
		user.setPassword("password");
		user.setEnabled(true);

		Owner spring = new Owner();
		spring.setId(RoomControllerTest.TEST_SPRING_ID);
		spring.setFirstName("spring");
		spring.setCity("Ejemplo");
		spring.setAddress("C/Ejemplo");
		spring.setLastName("spring");
		spring.setTelephone("671262689");
		spring.setUser(user);

		User userOwner = new User();
		userOwner.setUsername("owner");
		userOwner.setPassword("password");
		userOwner.setEnabled(true);

		Owner owner = new Owner();
		owner.setId(RoomControllerTest.TEST_OWNER_ID);
		owner.setFirstName("spring");
		owner.setCity("Ejemplo");
		owner.setAddress("C/Ejemplo");
		owner.setLastName("spring");
		owner.setTelephone("671262689");
		owner.setUser(userOwner);

		Status status = new Status();
		status.setId(1);
		status.setName("PENDING");

		Room room2 = new Room();
		room2.setId(RoomControllerTest.TEST_ROOM_WITHRESERVATION_ID);
		room2.setName("Room with Reser");
		room2.setCapacity(3);
		room2.setType(dogType);

		Reservation res = new Reservation();
		res.setId(RoomControllerTest.TEST_RESERVATION_ID);
		res.setEntryDate(LocalDate.now().plusDays(3));
		res.setExitDate(LocalDate.now().plusDays(9));
		res.setOwner(owner);
		res.setPet("14");
		res.setStatus(status);
		res.setRoom(room2);

		Set<Reservation> reservations = new HashSet<Reservation>();
		reservations.add(res);

		Set<Reservation> reservationsEmpty = new HashSet<Reservation>();

		room.setReservations(reservationsEmpty);
		room2.setReservations(reservations);

		Collection<String> roomNames = new ArrayList<String>();
		roomNames.add(room.getName());
		roomNames.add(room2.getName());
		
		BDDMockito.given(this.roomService.findAllRoomsName()).willReturn(roomNames);
		BDDMockito.given(this.reservationService.findReservationsById(TEST_RESERVATION_ID)).willReturn(res);
		BDDMockito.given(this.ownerService.findOwnerById(TEST_SPRING_ID)).willReturn(spring);
		BDDMockito.given(this.ownerService.findOwnerByUserName("owner")).willReturn(owner);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(dogType));
		BDDMockito.given(this.roomService.findRoomById(RoomControllerTest.TEST_ROOM_ID)).willReturn(room);
		BDDMockito.given(this.roomService.findRoomById(RoomControllerTest.TEST_ROOM_WITHRESERVATION_ID)).willReturn(room2);
	}

	@WithMockUser(value = "spring")
	@Test
	void testlistadoDeRomms() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("rooms")).andExpect(MockMvcResultMatchers.view().name("rooms/roomList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("room"))
			.andExpect(MockMvcResultMatchers.view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitChangeSitterForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/sitter", RoomControllerTest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("sitters"))
			.andExpect(MockMvcResultMatchers.view().name("rooms/selectSitter"));
	}

  @WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/new").param("id", "1")
								.with(csrf())
								.param("name", "Test Room2")
								.param("capacity", "2")
								.param("type", "dog"))
		.andExpect(status().is3xxRedirection());
	}


	@WithMockUser(value = "spring")
	@Test

	void testProcessCreationFormHasErrorsOnCapacityAndName() throws Exception {
		mockMvc.perform(post("/rooms/new")
				.with(csrf())
				.param("id", "1")
				.param("name","Test Room")
				.param("capacity", "0")
				.param("type","dog"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("room"))
		.andExpect(model().attributeHasFieldErrors("room","capacity","name"))
		.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsOnType() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "1").param("name", "Bad Room")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("type")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("room", "capacity", "type")).andExpect(MockMvcResultMatchers.view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateRoomForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}/edit", RoomControllerTest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("types"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("room")).andExpect(MockMvcResultMatchers.view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/edit", RoomControllerTest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Room Updated").param("capacity", "3").param("type", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/" + RoomControllerTest.TEST_ROOM_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacity() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/edit", RoomControllerTest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Failed Room").param("type", "dog"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("room")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("room", "capacity"))
			.andExpect(MockMvcResultMatchers.view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacityNull() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rooms/{roomId}/edit", RoomControllerTest.TEST_ROOM_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Failed Room").param("type", "dog").param("capacity", "0"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("room")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("room", "capacity"))
			.andExpect(MockMvcResultMatchers.view().name("/rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowRoomsWithouthReservations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerTest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Test Room")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(4))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type"))).andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowRoomsWithReservations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerTest.TEST_ROOM_WITHRESERVATION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room with Reser")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type"))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("reservations")))
			.andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testShowRoomsWithReservationsAndOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomId}", RoomControllerTest.TEST_ROOM_WITHRESERVATION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("name", Matchers.is("Room with Reser")))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("capacity", Matchers.is(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("type"))).andExpect(MockMvcResultMatchers.model().attribute("room", Matchers.hasProperty("reservations")))
			.andExpect(MockMvcResultMatchers.model().attributeExists("myReservations")).andExpect(MockMvcResultMatchers.view().name("rooms/roomDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteRoom() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete/{roomId}", RoomControllerTest.TEST_ROOM_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteRoomWithResercations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete/{roomId}", RoomControllerTest.TEST_ROOM_WITHRESERVATION_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/"));
	}
}