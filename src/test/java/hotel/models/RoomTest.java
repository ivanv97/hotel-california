package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.Bed;
import hotel.commodities.BedType;
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
	private Room room;

	@BeforeEach
	void setUp() {
		room = new Room(102, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE), new Toilet(), new Shower())));
	}

	@AfterEach
	void tearDown() {
		room.getBookings().clear();
		Hotel.getTakenRoomNumbers().clear();
	}

	@Test
	void setNumberShouldWorkWhenNumberIsUnique() {
		assertEquals(102, room.getNumber());
	}

	@Test
	void setNumberShouldNotWorkWhenNumberIsNotUnique() {
		//given, when
		Room room2 = new Room(room.getNumber(), null);
		//then
		assertNotEquals(room.getNumber(), room2.getNumber());
	}

	@Test
	void prepareCommodities() {
		//given
		LocalDate maintenanceDate = LocalDate.of(2019, 7, 14);
		//when
		room.prepareCommodities(maintenanceDate);
		//then
		assertTrue(room.getMaintenanceDates().contains(maintenanceDate));
	}

	@Test
	void removeBookingShouldWorkWhenRemovingExistingBooking() {
		//When
		room.createBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14), 1234567893L);

		//Then
		assertDoesNotThrow(() ->room.removeBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	@Test
	void removeBookingShouldNotWorkIfBookingNonExisting() {
		//When
		room.createBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14), 1234567893L);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> room.removeBooking(LocalDate.of(2024, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	@Test
	void checkIfBookedShouldReturnFalseIfNoBookings() {
		assertFalse(room.checkIfBooked(LocalDate.of(2019, 10, 21),
			LocalDate.of(2019, 10, 29)));
	}

	@Test
	void checkIfBookedShouldReturnTrueIfDatesNotFree() {
		//When
		room.createBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14), 1234567893L);

		//then
		assertTrue(room.checkIfBooked(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14)));
	}

	@Test
	void checkIfBookedShouldReturnFalseIfDatesAreFree() {
		//When
		room.createBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14), 1234567893L);

		//Then
		assertFalse(room.checkIfBooked(LocalDate.of(2020, 9, 15),
			LocalDate.of(2020, 9, 21)));
	}


	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnOnlySomeDates() {
		//given
		room.createBooking(LocalDate.of(2020, 9, 10),
			LocalDate.of(2020, 9, 14), 1234567893L);
		LocalDate endDate = LocalDate.of(2020, 9, 15);

		//when
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(LocalDate.of(2020, 9, 10),
			endDate, 1);

		//then
		assertTrue(availableDates.contains(endDate) && availableDates.contains(LocalDate.of(2020, 9, 14))
			&& availableDates.size() == 2);
	}

	@Test
	void hashCodeShouldBeDifferent() {
		//when
		Room room2 = new Room(103, null);
		//then
		assertNotEquals(room.hashCode(), room2.hashCode());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingBooksWithSameHash() {
		//when
		Room room2 = room;
		//then
		assertEquals(room, room2);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNullOrOtherType() {
		assertNotEquals(null, room);
		assertNotEquals("room", room);
	}
}
