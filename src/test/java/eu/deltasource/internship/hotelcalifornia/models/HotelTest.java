package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;
import eu.deltasource.internship.hotelcalifornia.commodities.*;
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
	private static final int ROOM_CAPACITY = 1;
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
		Hotel.getCommodityRoomMap().clear();
	}

	@Test
	void constructorShouldSetDefaultValuesWhenNoArguments() {
		//when
		Hotel hotelWithNull = new Hotel();
		//then
		assertEquals("unknown", hotelWithNull.getName());
		assertTrue(hotelWithNull.getRooms().isEmpty());
	}

	@Test
	void constructorShouldAssignFieldsWhenArgumentsPassed() {
		//then
		assertEquals("California", hotel.getName());
		assertFalse(hotel.getRooms().isEmpty());
	}

	@Test
	void getFreeRoomsAndDatesShouldThrowExIfDayNumberIsNotValid() {
		//then
		assertThrows(InvalidHotelActionException.class,
			() -> hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS + 4, ROOM_CAPACITY));
	}

	@Test
	void getFreeRoomsAndDatesShouldReturnEmptyListIfNotEnoughBeds() {
		//then
		assertThrows(InvalidHotelActionException.class, () -> hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS,
			ROOM_CAPACITY + 1));
	}

	@Test
	void getFreeRoomsAndDatesShouldWorkIfAvailableDates() {
		//when
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);
		Map<Room, AvailableDatesList> roomsAndDates = hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS, ROOM_CAPACITY);

		//then
		assertDoesNotThrow(() -> hotel.getFreeRoomsAndDates(FROM_DATE, TO_DATE, NUMBER_OF_DAYS, ROOM_CAPACITY));
		assertTrue(roomsAndDates.get(room).getAvailableDates().contains(FROM_DATE));
		assertEquals(1, roomsAndDates.get(room).getAvailableDates().size());
	}

	@Test
	void getFreeRoomsAndDatesShouldThrowExceptionIfNoAvailableDates() {
		//Given
		room.createBooking(FROM_DATE.minusDays(9), TO_DATE.plusDays(2), GUEST_ID);

		//When
		assertThrows(InvalidHotelActionException.class, () -> hotel.getFreeRoomsAndDates(FROM_DATE,
			TO_DATE.plusDays(1), NUMBER_OF_DAYS + 1, ROOM_CAPACITY));
	}

	@Test
	void findAvailableRoomsShouldReturnListIfAnyAreFree() {
		//Given
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//When
		List<Room> availableRooms = hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(5), ROOM_CAPACITY);

		//Then
		assertFalse(availableRooms.isEmpty());
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenNoneFreeFound() {
		//When
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(FROM_DATE.minusDays(2), TO_DATE.plusDays(1),
			ROOM_CAPACITY));
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenNotEnoughBeds() {
		//Given
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//When
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(5),
			ROOM_CAPACITY + 2));
	}

	@Test
	void findAvailableRoomsShouldThrowExWhenDatesNotChronological() {
		//When
		room.createBooking(FROM_DATE.plusDays(1), TO_DATE.plusDays(2), GUEST_ID);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAvailableRooms(TO_DATE.plusDays(3), TO_DATE.plusDays(1),
			ROOM_CAPACITY));
	}

	@Test
	void addRoomShouldWorkWhenRoomAddedIsDifferent() {
		//When
		Room room1 = new Room(102, new HashSet<>());

		//Then
		assertDoesNotThrow(() -> hotel.addRoom(room1));
	}

	@Test
	void addRoomShouldThrowExcWhenAddingTheSameRoom() {
		assertThrows(InvalidHotelActionException.class, () -> hotel.addRoom(room));
	}

	@Test
	void findAndBookFirstAvailableRoomShouldReturnNumberIfFreeRoom() {
		//When
		room.createBooking(FROM_DATE.minusDays(3), TO_DATE, GUEST_ID);

		//Then
		assertEquals(room.getNumber(), hotel.findAndBookFirstAvailableRoom(TO_DATE,
			TO_DATE.plusDays(2), ROOM_CAPACITY, GUEST_ID));
	}

	@Test
	void findAndBookFirstAvailableRoomShouldThrowExcIfNoAvailableRooms() {
		//When
		room.createBooking(FROM_DATE.minusDays(3), TO_DATE, GUEST_ID);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> hotel.findAndBookFirstAvailableRoom(FROM_DATE,
			TO_DATE.plusDays(2), ROOM_CAPACITY, GUEST_ID));
	}
}
