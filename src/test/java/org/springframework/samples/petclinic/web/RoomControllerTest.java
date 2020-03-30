package org.springframework.samples.petclinic.web;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

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
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import java.awt.print.Printable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = RoomController.class,
			includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration = SecurityConfiguration.class)
class RoomControllerTest {
	
	public static final int TEST_ROOM_ID = 1;
	public static final int TEST_OWNER_ID = 2;
	private static final int TEST_SPRING_ID = 1;
	public static final int TEST_ROOM_WITHRESERVATION_ID = 2;
	public static final int TEST_RESERVATION_ID = 1;
	
	
	@Autowired
	private RoomController roomController;
	
	@MockBean
	private RoomService roomService;
		@MockBean
		private ReservationService reservationService;

		@MockBean
		private OwnerService ownerService;
	
		@MockBean
		private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(value = "spring")
	@BeforeEach
	void setup() {
		PetType dogType = new PetType();
		dogType.setId(2);
		dogType.setName("dog");
		
		Room room = new Room();
		room.setId(TEST_ROOM_ID);
		room.setCapacity(4);
		room.setName("Test Room");
		room.setType(dogType);
		
		User user = new User();
		user.setUsername("spring");
		user.setPassword("password");
		user.setEnabled(true);
		
		Owner spring = new Owner();
		spring.setId(TEST_SPRING_ID);
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
		owner.setId(TEST_OWNER_ID);
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
		room2.setId(TEST_ROOM_WITHRESERVATION_ID);
		room2.setName("Room with Reser");
		room2.setCapacity(3);
		room2.setType(dogType);
		
		Reservation res = new Reservation();
		res.setId(TEST_RESERVATION_ID);
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
		
		
		BDDMockito.given(this.reservationService.findReservationsById(TEST_RESERVATION_ID)).willReturn(res);
		BDDMockito.given(this.ownerService.findOwnerById(TEST_SPRING_ID)).willReturn(spring);
		BDDMockito.given(this.ownerService.findOwnerByUserName("owner")).willReturn(owner);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(dogType));
		BDDMockito.given(this.roomService.findRoomById(TEST_ROOM_ID)).willReturn(room);
		BDDMockito.given(this.roomService.findRoomById(TEST_ROOM_WITHRESERVATION_ID)).willReturn(room2);
	}
	
	@WithMockUser(value="spring")
	@Test
	void testlistadoDeRomms() throws Exception {
		mockMvc.perform(get("/rooms")).andExpect(status().isOk())
			   .andExpect(model().attributeExists("rooms"))
			   .andExpect(view().name("rooms/roomList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception{
		mockMvc.perform(get("/rooms/new")).andExpect(status().isOk()).andExpect(model().attributeExists("room"))
		.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/new").param("id", "1")
								.with(csrf())
								.param("name", "Test Room")
								.param("capacity", "2")
								.param("type", "dog"))
		.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value="spring")
	@Test
	void testProcessCreationFormHasErrorsOnCapacity() throws Exception {
		mockMvc.perform(post("/rooms/new")
				.with(csrf())
				.param("id", "1")
				.param("name","Bad Room")
				.param("capacity", "0")
				.param("type","dog"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("room"))
		.andExpect(model().attributeHasFieldErrors("room","capacity"))
		.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
	@WithMockUser(value="spring")
	@Test
	void testProcessCreationFormHasErrorsOnType() throws Exception {
		mockMvc.perform(post("/rooms/new")
				.with(csrf())
				.param("id", "1")
				.param("name","Bad Room"))
		.andExpect(status().isOk())
		.andExpect(model().attributeDoesNotExist("type"))
		.andExpect(model().attributeHasFieldErrors("room", "capacity","type"))
		.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testInitUpdateRoomForm() throws Exception {
		mockMvc.perform(get("/rooms/{roomId}/edit",TEST_ROOM_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("types"))
				.andExpect(model().attributeExists("room"))
				.andExpect(view().name("/rooms/createOrUpdateRoomForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/{roomId}/edit",TEST_ROOM_ID)
						.with(csrf())
						.param("name", "Room Updated")
						.param("capacity", "3")
						.param("type", "dog"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rooms/"+TEST_ROOM_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacity() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/edit",TEST_ROOM_ID)
						.with(csrf())
						.param("name", "Failed Room")
						.param("type", "dog"))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeHasErrors("room"))
			   .andExpect(model().attributeHasFieldErrors("room","capacity"))
			   .andExpect(view().name("/rooms/createOrUpdateRoomForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormHasErrorsOnCapacityNull() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/edit",TEST_ROOM_ID)
						.with(csrf())
						.param("name", "Failed Room")
						.param("type", "dog")
						.param("capacity","0"))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeHasErrors("room"))
			   .andExpect(model().attributeHasFieldErrors("room","capacity"))
			   .andExpect(view().name("/rooms/createOrUpdateRoomForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowRoomsWithouthReservations() throws Exception { 
		mockMvc.perform(get("/rooms/{roomId}",TEST_ROOM_ID))
			.andExpect(status().isOk())
			.andExpect(model().attribute("room", hasProperty("name", is("Test Room"))))
			.andExpect(model().attribute("room", hasProperty("capacity",is(4))))
			.andExpect(model().attribute("room", hasProperty("type")))
			.andExpect(view().name("rooms/roomDetails"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testShowRoomsWithReservations() throws Exception { 
		mockMvc.perform(get("/rooms/{roomId}",TEST_ROOM_WITHRESERVATION_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("room", hasProperty("name", is("Room with Reser"))))
			   .andExpect(model().attribute("room", hasProperty("capacity",is(3))))
			   .andExpect(model().attribute("room", hasProperty("type")))
			   .andExpect(model().attribute("room", hasProperty("reservations")))
			   .andExpect(view().name("rooms/roomDetails"));
	}
	
	@WithMockUser(value="owner")
	@Test
	void testShowRoomsWithReservationsAndOwner() throws Exception { 
		mockMvc.perform(get("/rooms/{roomId}",TEST_ROOM_WITHRESERVATION_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("room", hasProperty("name", is("Room with Reser"))))
			   .andExpect(model().attribute("room", hasProperty("capacity",is(3))))
			   .andExpect(model().attribute("room", hasProperty("type")))
			   .andExpect(model().attribute("room", hasProperty("reservations")))
			   .andExpect(model().attributeExists("myReservations"))
			   .andExpect(view().name("rooms/roomDetails"));
	}
	
	
	@WithMockUser(value="spring")
	@Test
	void testProcessDeleteRoom() throws Exception {
		mockMvc.perform(get("/rooms/delete/{roomId}",TEST_ROOM_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/rooms/"));
	}
	@WithMockUser(value="spring")
	@Test
	void testProcessDeleteRoomWithResercations() throws Exception {
		mockMvc.perform(get("/rooms/delete/{roomId}",TEST_ROOM_WITHRESERVATION_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/rooms/"));
	}
}
