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
	private static final LocalDate FROM_DATE = LocalDate.of(2020, 9, 10);
	private static final LocalDate TO_DATE = LocalDate.of(2020, 9, 14);
	private static final long EGN = 1234567893L;

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
		//when
		room.prepareCommodities(FROM_DATE);
		//then
		assertTrue(room.getMaintenanceDates().contains(FROM_DATE));
	}

	@Test
	void removeBookingShouldWorkWhenRemovingExistingBooking() {
		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//Then
		assertDoesNotThrow(() -> room.removeBooking(FROM_DATE, TO_DATE));
	}

	@Test
	void removeBookingShouldNotWorkIfBookingNonExisting() {
		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> room.removeBooking(FROM_DATE.plusDays(1), TO_DATE));
	}

	@Test
	void checkIfBookedShouldReturnFalseIfNoBookings() {
		assertFalse(room.checkIfBooked(FROM_DATE, TO_DATE));
	}

	@Test
	void checkIfBookedShouldReturnTrueIfDatesNotFree() {
		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//then
		assertTrue(room.checkIfBooked(FROM_DATE.plusDays(1), TO_DATE));
		assertTrue(room.checkIfBooked(FROM_DATE, TO_DATE.minusDays(1)));
	}

	@Test
	void checkIfBookedShouldReturnFalseIfDatesAreFree() {
		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//Then
		assertFalse(room.checkIfBooked(TO_DATE, TO_DATE.plusDays(5)));
		assertFalse(room.checkIfBooked(FROM_DATE.minusDays(5),FROM_DATE));
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnOnlySomeDates() {
		//given
		room.createBooking(FROM_DATE, TO_DATE, EGN);
		//when
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE.plusDays(1), 1);

		//then
		assertTrue(availableDates.contains(TO_DATE.plusDays(1)) && availableDates.contains(TO_DATE)
			&& availableDates.size() == 2);
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnEmptyListIfNoDates() {
		//given
		room.createBooking(FROM_DATE, TO_DATE, EGN);
		//when
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE.minusDays(1), 1);

		//then
		assertTrue(availableDates.isEmpty());
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnEmptyListIfBedNotAvailable() {
		//given
		room.createBooking(FROM_DATE, TO_DATE, EGN);
		//when
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE.plusDays(1), 2);

		//then
		assertTrue(availableDates.isEmpty());
	}

	@Test
	void checkIfEnoughBedsShouldReturnFalseIfNotEnough(){
		assertFalse(room.checkIfEnoughBeds(2));
	}

	@Test
	void checkIfEnoughBedsShouldReturnTrueIfNumberOfBedsEqualsRequired(){
		assertTrue(room.checkIfEnoughBeds(1));
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
