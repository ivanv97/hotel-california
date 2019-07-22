package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;
import eu.deltasource.internship.hotelcalifornia.commodities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
	private static Hotel hotel;
	private static Manager manager;

	@BeforeAll
	static void setUp() {
		hotel = new Hotel();
		manager = new Manager("Ivan");
		manager.setHotel(hotel);
		Hotel.getTakenRoomNumbers().clear();
	}

	@AfterEach
	void tearDown() {
		hotel.getRooms().clear();
		Hotel.getTakenRoomNumbers().clear();
		hotel.getBookedRoomsNumbers().clear();
	}

	@Test
	void bookThreeRooms() {
		//given
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
			new Shower(), new Toilet()));

		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
			new Bed(BedType.SINGLE), new Bed(BedType.SINGLE), new Shower(), new Toilet(), new Toilet()));

		Set<AbstractCommodity> thirdRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
			new Bed(BedType.SINGLE), new Toilet(), new Shower()));

		hotel.addRoom(new Room(101, firstRoomCommodities));
		hotel.addRoom(new Room(102, secondRoomCommodities));
		hotel.addRoom(new Room(103, thirdRoomCommodities));


		assertDoesNotThrow(() -> manager.createBooking(LocalDate.of(2019, 7, 20),
			LocalDate.of(2019, 7, 25), 2, 1234567899L));

		assertDoesNotThrow(() -> manager.createBooking(LocalDate.of(2019, 7, 22),
			LocalDate.of(2019, 7, 23), 1, 1234567898L));

		assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(
			LocalDate.of(2019, 7, 19),
			LocalDate.of(2019, 7, 21), 2, 1234567898L));

	}

	@Test
	void createBookingsCustomTaskTestScenarioOne() {
		//Given
		Room room = new Room(101, new HashSet<>(Arrays.asList(new Bed(BedType.DOUBLE))));
		hotel.addRoom(room);

		//When
		int bookedRoomNumber;
		bookedRoomNumber = manager.createBooking(LocalDate.of(2019, 1, 1),
			LocalDate.of(2019, 1, 2), 2, 1234567891L);

		//Then
		assertEquals(101, bookedRoomNumber);
	}

	@Test
	void createBookingsCustomTaskTestScenarioTwo() {
		assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(LocalDate.of(2019, 1, 1),
			LocalDate.of(2019, 1, 2), 2, 1234567891L));
	}

	@Test
	void createBookingsCustomTaskTestScenarioThree() {
		//Given
		Room room = new Room(101, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE))));
		hotel.addRoom(room);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(LocalDate.of(2019, 1, 1),
			LocalDate.of(2019, 1, 2), 2, 1234567891L));
	}

	@Test
	void removeBookingShouldSucceedIfProperParameters() {
		//Given
		Room room = new Room(101, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE))));
		hotel.addRoom(room);

		//When
		room.createBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 2), 1234567891L);

		//Then
		assertDoesNotThrow(() -> manager.removeBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 2), 101));
	}

	@Test
	void removeBookingShouldThrowExcIfBookingNonExisting() {
		//Given
		Room room = new Room(101, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE))));
		hotel.addRoom(room);

		//When
		room.createBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 2), 1234567891L);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> manager.removeBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 3), 101));
	}

	@Test
	void removeBookingShouldThrowExcIfRoomNumberNotExisting() {
		//Given
		Room room = new Room(101, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE))));
		hotel.addRoom(room);

		//When
		room.createBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 2), 1234567891L);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> manager.removeBooking(LocalDate.of(2020, 1, 1),
			LocalDate.of(2020, 1, 2), 102));
	}

}
