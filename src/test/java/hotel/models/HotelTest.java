package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
	private Set<AbstractCommodity> roomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
		new Shower(), new Toilet()));
	private Room room = new Room(101, roomCommodities);
	private Hotel hotel;

	@BeforeEach
	void setUp(){
		hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room)));
	}

	@AfterEach
	void tearDown(){
		hotel.getRooms().clear();
	}

	@Test
	void constructorShouldSetDefaultValuesWhenNullArguments() {
		//when
		Hotel hotelWithNull = new Hotel(null, null);
		//then
		assertEquals("Unknown", hotelWithNull.getName());
		assertTrue(hotelWithNull.getRooms().isEmpty());
	}

	@Test
	void constructorShouldAssignFieldsWhenValidArguments() {
		//then
		assertEquals("California", hotel.getName());
		assertFalse(hotel.getRooms().isEmpty());
	}

	@Test
	void getFreeRoomsAndDatesShouldThrowExIfDayNumberIsNotValid() {
		//then
		assertThrows(InvalidHotelActionException.class, () -> hotel.getFreeRoomsAndDates(
			LocalDate.of(2019, 7, 18),
			LocalDate.of(2019, 7, 25), 5, 1));
	}

	@Test
	void getFreeRoomsAndDatesShouldWorkIfAvailableDates() {
		Map<Room, AvailableDatesList> roomsAndDates = null;
		//when
		room.createBooking(LocalDate.of(2019, 07, 20),
				LocalDate.of(2019, 07, 22),1234567895L);
		roomsAndDates = hotel.getFreeRoomsAndDates(LocalDate.of(2019, 7, 19),
				LocalDate.of(2019, 7, 20), 1, 1);

		//then
		assertDoesNotThrow(() -> hotel.getFreeRoomsAndDates(LocalDate.of(2019, 7, 19),
			LocalDate.of(2019, 7, 20), 1, 1));
		assertTrue(roomsAndDates.get(room).getList().contains(LocalDate.of(2019, 7, 19))
			&& roomsAndDates.get(room).getList().size() == 1);
	}

	@Test
	void findAvailableRoomsShouldReturnListIfAnyAreFree(){
		//Given
		room.createBooking(LocalDate.of(2019, 07, 20),
			LocalDate.of(2019, 07, 22),1234567895L);

		//When
		List<Room> availableRooms = hotel.findAvailableRooms(LocalDate.of(2019, 07,23),
			LocalDate.of(2019, 07,25),1, new ArrayList<>());

		//Then
		assertFalse(availableRooms.isEmpty());
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenNoneFreeFound(){
		//When
		room.createBooking(LocalDate.of(2019, 07, 20),
			LocalDate.of(2019, 07, 22),1234567895L);

		//Then
		assertThrows(InvalidHotelActionException.class, () ->hotel.findAvailableRooms(
			LocalDate.of(2019, 07,17), LocalDate.of(2019, 07,21),
			1, new ArrayList<>()));
	}
}
