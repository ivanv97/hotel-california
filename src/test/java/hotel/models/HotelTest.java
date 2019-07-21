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
	private static final LocalDate FROM_DATE = LocalDate.of(2019, 7, 19);
	private static final LocalDate TO_DATE = LocalDate.of(2019, 7, 20);
	private static final int NUMBER_OF_DAYS = 1;
	private static final int ACTUAL_BED_NUMBER = 1;
	private static final long GUEST_ID = 1234567895L;
	private Set<AbstractCommodity> roomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
		new Shower(), new Toilet()));
	private Room room = new Room(101, roomCommodities);
	private Hotel hotel;

	@BeforeEach
	void setUp() {
		hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room)));
	}

	@AfterEach
	void tearDown() {
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
		assertThrows(InvalidHotelActionException.class,
			() -> hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS + 4, ACTUAL_BED_NUMBER));
	}

	@Test
	void getFreeRoomsAndDatesShouldReturnEmptyMapIfNotEnoughBeds() {
		//then
		Map<Room, AvailableDatesList> freeRoomsAndDates = hotel.getFreeRoomsAndDates(FROM_DATE,
			TO_DATE, NUMBER_OF_DAYS, ACTUAL_BED_NUMBER + 1);
		assertTrue(freeRoomsAndDates.isEmpty());
	}

	@Test
	void getFreeRoomsAndDatesShouldWorkIfAvailableDates() {
		Map<Room, AvailableDatesList> roomsAndDates = null;
		//when
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);
		roomsAndDates = hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS, ACTUAL_BED_NUMBER);

		//then
		assertDoesNotThrow(() -> hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, 1, 1));
		assertTrue(roomsAndDates.get(room).getAvailableDates().contains(FROM_DATE)
			&& roomsAndDates.get(room).getAvailableDates().size() == 1);
	}

	@Test
	void getFreeRoomsAndDatesShouldReturnEmptyListIfNoAvailableDates() {
		Map<Room, AvailableDatesList> roomsAndDates = null;
		//when
		room.createBooking(FROM_DATE.minusDays(9), TO_DATE.plusDays(2), GUEST_ID);
		roomsAndDates = hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS, ACTUAL_BED_NUMBER);

		//then
		assertTrue(roomsAndDates.isEmpty());
	}

	@Test
	void findAvailableRoomsShouldReturnListIfAnyAreFree() {
		//Given
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//When
		List<Room> availableRooms = hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(5), ACTUAL_BED_NUMBER,
			new ArrayList<>());

		//Then
		assertFalse(availableRooms.isEmpty());
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenNoneFreeFound() {
		//When
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(FROM_DATE.minusDays(2), TO_DATE.plusDays(1),
			ACTUAL_BED_NUMBER, new ArrayList<>()));
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenNotEnoughBeds() {
		//Given
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//When
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(5),
			ACTUAL_BED_NUMBER + 2, new ArrayList<>()));
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenDatesNotChronological() {
		//When
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(1),
			ACTUAL_BED_NUMBER, new ArrayList<>()));
	}
}
