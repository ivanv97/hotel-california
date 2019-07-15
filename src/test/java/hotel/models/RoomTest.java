package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.Bed;
import hotel.commodities.Shower;
import hotel.commodities.Toilet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
	Room room;

	@BeforeEach
	void setUp() {
		room = new Room(102, new HashSet<>(Arrays.asList(new Bed(), new Toilet(), new Shower())));
	}

	@AfterEach
	void tearDown() {
		Hotel.getTakenRoomNumbers().clear();
	}

	/**
	 * Tests if we can instantiate another room
	 * with a different number - test passes if
	 * the newly assigned number is the same as intended
	 */
	@Test
	void setNumberShouldWorkWhenNumberIsUnique() {
		assertEquals(102, room.getNumber());
	}

	/**
	 * Test passes if we are not allowed to
	 * create a room object with the same number
	 * as the first one
	 */
	@Test
	void setNumberShouldNotWorkWhenNumberIsNotUnique() {
		Room room2 = new Room(room.getNumber(), null);
		assertNotEquals(room.getNumber(), room2.getNumber());
	}

	/**
	 * If commodities are really prepared there
	 * should be a date registered in maintenance dates
	 */
	@Test
	void prepareCommodities() {
		LocalDate maintenanceDate = LocalDate.of(2019, 7, 14);
		room.prepareCommodities(maintenanceDate);
		assertTrue(room.getMaintenanceDates().contains(maintenanceDate));
	}

	/**
	 * Test passes if dates are free
	 */
	@Test
	void createBookingShouldSucceedIfProperArgumentsPassed() {
		assertDoesNotThrow(() -> room.createBooking(LocalDate.of(2019, 1, 10),
			LocalDate.of(2019, 1, 14), "Ivan", 1234567893L));
	}

	/**
	 * If there already is a booking for one or more of the dates
	 * we are not allowed to make a new booking
	 */
	@Test
	void createBookingShouldNotWorkWhenAlreadyBooked() {
		try {
			room.createBooking(LocalDate.of(2019, 9, 10),
				LocalDate.of(2019, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertThrows(InvalidHotelActionException.class, () -> room.createBooking(LocalDate.of(2019, 9, 10),
			LocalDate.of(2019, 9, 11), "Ivan", 1234567893L));
	}

	/**
	 * If there is a booking with the specified dates
	 * as arguments - it can be removed
	 */
	@Test
	void removeBookingShouldWorkWhenRemovingExistingBooking() {
		try {
			room.createBooking(LocalDate.of(2020, 9, 10),
				LocalDate.of(2020, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertDoesNotThrow(() -> room.removeBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	/**
	 * If a booking with the particular dates
	 * does not exist it cannot be removed
	 */
	@Test
	void removeBookingShouldThrowExcWhenBookingIsNonExisting() {
		try {
			room.createBooking(LocalDate.of(2020, 9, 10),
				LocalDate.of(2020, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertThrows(InvalidHotelActionException.class, () -> room.removeBooking(LocalDate.of(2024, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	/**
	 * If there are no bookings on that room -
	 * all of the dates are free
	 */
	@Test
	void checkIfBookedShouldReturnFalseIfNoBookings() {
		assertFalse(room.checkIfBooked(LocalDate.of(2019, 10, 21), LocalDate.of(2019, 10, 29)));
	}

	/**
	 * If the desired date/dates are not free -
	 * then the room is booked
	 */
	@Test
	void checkIfBookedShouldReturnTrueIfDatesNotFree() {
		try {
			room.createBooking(LocalDate.of(2020, 9, 10),
				LocalDate.of(2020, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertTrue(room.checkIfBooked(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	/**
	 * If the desired dates are free -
	 * the method should return false - not booked
	 */
	@Test
	void checkIfBookedShouldReturnFalseIfDatesAreFree() {
		try {
			room.createBooking(LocalDate.of(2020, 9, 10),
				LocalDate.of(2020, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertFalse(room.checkIfBooked(LocalDate.of(2020, 9, 15),
			LocalDate.of(2020, 9, 21)));
	}

	/**
	 * Should return only the free dates in a specified interval
	 * ignoring the dates that are booked
	 */
	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnOnlySomeDates() {
		try {
			room.createBooking(LocalDate.of(2020, 9, 10),
				LocalDate.of(2020, 9, 14), "Ivan", 1234567893L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		LocalDate endDate = LocalDate.of(2020, 9, 15);
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(LocalDate.of(2020, 9, 10),
			endDate, 1);
		assertTrue(availableDates.contains(endDate) && availableDates.contains(LocalDate.of(2020, 9, 14))
			&& availableDates.size() == 2);
	}

	/**
	 * Returns different values for two different room objects
	 */
	@Test
	void hashCodeShouldBeDifferent() {
		Room room2 = new Room(103, null);
		assertNotEquals(room.hashCode(), room2.hashCode());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingBooksWithSameHash() {
		Room room2 = room;
		assertTrue(room.equals(room2));
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNullOrOtherType() {
		assertFalse(room.equals(null));
		assertFalse(room.equals("room"));
	}
}
