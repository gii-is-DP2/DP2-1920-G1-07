package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Reservation;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservationService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers=ReservationController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class ReservationControllerTest {
	
	
	public static final int TEST_RESERVATION_ID = 1;
	public static final int TEST_RESERVATIONACCEPTED_ID = 2;
	public static final int TEST_OWNER_ID = 1;
	public static final int TEST_ROOM_ID = 1;
	public static final int TEST_ROOMCOMPLETED_ID = 2;
	public static final int TEST_STATUS_ID = 1;
	public static final int TEST_STATUSACCEPTED_ID = 2;
	public static final int TEST_PETSnake_ID = 1;
	public static final int TEST_PETDog_ID = 2;
	
	@MockBean
	public ReservationService resService;
		
		@MockBean
		public OwnerService ownerService; 
		
		@MockBean
		public RoomService roomService;
		
		@MockBean
		public PetService petService;
		
		@MockBean
		public UserService userService;
		
	@Autowired
	public MockMvc mockMvc;
	
	public Reservation reservation;
	
	@BeforeEach
	void setup() {
		PetType dogType = new PetType();
		dogType.setId(1);
		dogType.setName("dog");
		
		PetType snakeType = new PetType();
		snakeType.setId(2);
		snakeType.setName("snake");
		
		Room room = new Room();
		room.setId(TEST_ROOM_ID);
		room.setCapacity(4);
		room.setName("Test Room");
		room.setType(dogType);
		
		Room roomCompleted = new Room();
		roomCompleted.setId(TEST_ROOMCOMPLETED_ID);
		roomCompleted.setCapacity(1);
		roomCompleted.setName("Test Room Completed");
		roomCompleted.setType(dogType);
		
		
		Pet snake = new Pet();
		snake.setId(TEST_PETSnake_ID);
		snake.setName("Pet Snake");
		snake.setType(snakeType);
		snake.setBirthDate(LocalDate.now());
		
		Pet dog = new Pet();
		dog.setId(TEST_PETDog_ID);
		dog.setName("Pet Dog");
		dog.setBirthDate(LocalDate.now());
		dog.setType(dogType);
		
		User user = new User();
		user.setUsername("spring");
		user.setPassword("password");
		user.setEnabled(true);
		
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.setUser(user);
		
		Status statusPending = new Status();
		statusPending.setId(TEST_STATUS_ID);
		statusPending.setName("PENDING");
		
		Status statusAccepted = new Status();
		statusAccepted.setId(TEST_STATUSACCEPTED_ID);
		statusAccepted.setName("ACCEPTED");
		
		reservation = new Reservation();
		reservation.setId(TEST_RESERVATION_ID);
		reservation.setEntryDate(LocalDate.of(2020, 07, 19));
		reservation.setExitDate(LocalDate.of(2020, 07, 29));
		reservation.setOwner(owner);
		reservation.setPet("2");
		reservation.setStatus(statusPending);
		reservation.setRoom(room);
		
		
		Reservation reser = new Reservation();
		reser.setId(TEST_RESERVATIONACCEPTED_ID);
		reser.setEntryDate(LocalDate.of(2020, 07, 19));
		reser.setExitDate(LocalDate.of(2020, 07, 29));
		reser.setOwner(owner);
		reser.setPet("15");
		reser.setStatus(statusAccepted);
		reser.setRoom(roomCompleted);
		
		Set<Reservation> reservations = Lists.newArrayList(reser).stream().collect(Collectors.toSet());
		roomCompleted.setReservations(reservations);
		
		Collection<PetType> petTypes = new ArrayList<PetType>();
		petTypes.add(snakeType);
		petTypes.add(dogType);
		
		BDDMockito.given(this.petService.findPetById(TEST_PETSnake_ID)).willReturn(snake);
		BDDMockito.given(this.petService.findPetById(TEST_PETDog_ID)).willReturn(dog);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(petTypes);
		BDDMockito.given(this.roomService.findRoomById(TEST_ROOM_ID)).willReturn(room);
		BDDMockito.given(this.roomService.findRoomById(TEST_ROOMCOMPLETED_ID)).willReturn(roomCompleted);
		BDDMockito.given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
		BDDMockito.given(this.userService.findUserByUserName("spring")).willReturn(user);
		BDDMockito.given(this.resService.findStatusById(TEST_STATUS_ID)).willReturn(statusPending);
		BDDMockito.given(this.resService.findStatusById(TEST_STATUSACCEPTED_ID)).willReturn(statusAccepted);
		BDDMockito.given(this.resService.findReservationsById(TEST_RESERVATION_ID)).willReturn(reservation);
		BDDMockito.given(this.resService.findReservationsById(TEST_RESERVATIONACCEPTED_ID)).willReturn(reser);
		
	}
	/**
	 * @author amine
	 * Debe generar correctamente la vista para la creacion de una nueva reserva
	 * @return vista de formulario para crear una nueva reservation: reservations/createReservationForm
	 */
	@WithMockUser(value = "spring")
	@Test
	void testInitNewReservationForm() throws Exception { 
		mockMvc.perform(get("/rooms/{roomId}/reservations/new",TEST_ROOM_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("reservation","status"))
			   .andExpect(view().name("reservations/createReservationForm"));
	}
	
	/**
	 * @author amine
	 * Debe crear correctamente la Reservation.
	 * @return vista de la nueva room creada /rooms/TEST_ROOM_ID
	 */
	@WithMockUser(value = "spring")
	@Test
	void testProcessNewReservationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/11")
				.param("exitDate","2020/07/15")
				.param("pet", "2")) //Dog
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	
	/**
	 * @author amine
	 * Debe generar error debido intentando crear una Reservation con la fecha de entrada y de salida en pasado.
	 * @return vista de formulario /reservations/createReservationForm  
	 */
	
	@WithMockUser(value="spring")
	@Test
	void testProcessNewReservationFormHasErrorsOnDate() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/03/24")
				.param("exitDate", "2020/03/25")
				.param("pet", "2"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "entryDate"))
		.andExpect(model().attributeHasFieldErrors("reservation", "exitDate"))
		.andExpect(view().name("reservations/createReservationForm"));
	}
	
	/**
	 * @author amine
	 * Debe generar error debido a que la room con TEST_ROOM_ID tiene un PetType de tipo Dog que tiene en la BBDD id = 2
	 * y en este caso estamos intentando crear una Reservation para un pet de id 1 que en la BBDD es un Cat, por lo tanto
	 * el petType de la Room y el del Pet de la Reservation no son compatibles.
	 * @return vista de formulario /reservations/createReservationForm  
	 */
	@WithMockUser(value="spring")
	@Test
	void testProcessNewReservationFormHasErrorsOnPet() throws Exception{
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/24")
				.param("exitDate", "2020/07/28")
				.param("pet", "1"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "pet"))
		.andExpect(view().name("reservations/createReservationForm"));
	
	}
	
	/**
	 * @author amine
	 * Debe generar error debido a que  estamos intentando crear una Reservation sin indicar el Pet para el 
	 * cual queremos realizar la reserva.
	 * @return vista de formulario /reservations/createReservationForm  
	 */
	@WithMockUser(value="spring")
	@Test
	void testProcessNewReservationFormHasErrorsPetNull() throws Exception{
		mockMvc.perform(post("/rooms/{roomId}/reservations/new",TEST_ROOM_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/07/24")
				.param("exitDate", "2020/07/28"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "pet"))
		.andExpect(view().name("reservations/createReservationForm"));
	
	}
	/**
	 * @author amine
	 * Debe generar correctamente la vista para la edicion de la reserva para una room que no este Completa por 
	 * parte del admin es decir que no tenga un nuemero de reservas aceptada igual a su capacidad
	 * @return vista de formulario /reservations/updateReservationForm  
	 */
	@WithMockUser(value = "spring")
	@Test
	void testInitProcessUpdateReservationFormToNotComletedRoom() throws Exception{
		mockMvc.perform(get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM_ID,TEST_OWNER_ID,TEST_RESERVATION_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("editreservation","status"))
			   .andExpect(view().name("reservations/updateReservationForm"));
	}
	/**
	 * @author amine
	 * Debe generar correctamente la vista para la edicion de la reserva para una room por parte del administrador,
	 * en estos casos, la room esta completa porque su capacidad es igual a las reservas ya aceptadas
	 * Entonces el sistema espera tres atributos, editreservation, completedRoom
	 * y statusWithaouthAccepted 
	 */
	@WithMockUser(value = "spring")
	@Test
	void testInitProcessUpdateReservationFormToAComletedRoom() throws Exception{
		mockMvc.perform(get("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOMCOMPLETED_ID,TEST_OWNER_ID,TEST_RESERVATIONACCEPTED_ID))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeExists("editreservation","completedRoom","statusWithouthAccepted"))
			   .andExpect(view().name("reservations/updateReservationForm"));
	}
	/**
	 * @author amine
	 * Debe editar correctamente la Reservation.
	 * @return vista de la nueva room creada /rooms/TEST_ROOM_ID
	 */
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateReservationFormSuccess() throws Exception { 
		mockMvc.perform(post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM_ID,TEST_OWNER_ID,TEST_RESERVATION_ID)
					.with(csrf())
					.param("id", "1")
					.param("entryDate", "2021/07/19")
					.param("exitDate", "2021/07/29")
					.param("pet", "2"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	
	/**
	 * @author amine
	 * Debe generar error debido a que la room con TEST_ROOM_ID tiene un PetType de tipo Dog que tiene en la BBDD id = 2
	 * y en este caso estamos intentando crear una Reservation para un pet de id 1 que en la BBDD es un Cat, por lo tanto
	 * el petType de la Room y el del Pet de la Reservation no son compatibles.
	 * Ademas las fechas de entrada (entryDate) y de salida (exitDate) estan en pasado
	 * El sistema espera errores en la entidad reservation y en los atributos entryDate,exitDate y pet.
	 * @return vista de formulario /reservations/updateReservationForm  
	 */
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateReservationFormErrorsOnDateAndPet() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit",TEST_ROOM_ID,TEST_OWNER_ID,TEST_RESERVATION_ID)
				.with(csrf())
				.param("id", "1")
				.param("entryDate", "2020/02/19")//Pasado debe petar
				.param("exitDate", "2020/02/29")//Pasado debe petar
				.param("pet", "1")) //cat mientras el type de Room es dog (debe petar)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("reservation"))
		.andExpect(model().attributeHasFieldErrors("reservation", "entryDate", "exitDate","pet"))
		.andExpect(view().name("reservations/updateReservationForm"));
	}
	/**
	 * @author amine
	 * Debe eliminar correctamente una reserva
	 * @return vista de formulario /reservations/TEST_ROOM_ID  
	 */
	@WithMockUser(value="spring")
	@Test
	void testProcessDeleteReservation() throws Exception {
	mockMvc.perform(get("/rooms/{roomId}/reservation/{reservationId}/delete",TEST_ROOM_ID,TEST_RESERVATION_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rooms/{roomId}"));
	}
	
		
}
