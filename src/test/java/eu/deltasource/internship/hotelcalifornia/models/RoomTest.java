package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.BookingActionException;
import eu.deltasource.internship.hotelcalifornia.commodities.Bed;
import eu.deltasource.internship.hotelcalifornia.commodities.BedType;
import eu.deltasource.internship.hotelcalifornia.commodities.Shower;
import eu.deltasource.internship.hotelcalifornia.commodities.Toilet;
import eu.deltasource.internship.hotelcalifornia.customexceptions.NullCommodityException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
	private Room room;
	private static final LocalDate FROM_DATE = LocalDate.of(2020, 9, 10);
	private static final LocalDate TO_DATE = LocalDate.of(2020, 9, 14);
	private static final long EGN = 1234567893L;
	private static final int ROOM_NUMBER = 102;

	@BeforeEach
	void setUp() {
		room = new Room(ROOM_NUMBER, new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE), new Toilet(), new Shower())));
	}

	@AfterEach
	void tearDown() {
		room.getBookings().clear();
		room.getCommodities().clear();
		room.getMaintenanceDates().clear();
		Hotel.getTakenRoomNumbers().clear();
	}

	@Test
	void setNumberShouldWorkWhenNumberIsUnique() {
		assertEquals(ROOM_NUMBER, room.getNumber());
	}

	@Test
	void setNumberShouldNotWorkWhenNumberIsNotUnique() {
		//given, when
		Room room2 = new Room(ROOM_NUMBER, new HashSet<>());

		//then
		assertNotEquals(ROOM_NUMBER, room2.getNumber());
	}

	@Test
	void prepareCommoditiesShouldAddMaintenanceDate() {
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
		assertThrows(BookingActionException.class, () -> room.removeBooking(FROM_DATE.plusDays(1), TO_DATE));
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
		assertTrue(room.checkIfBooked(FROM_DATE, TO_DATE));
		assertTrue(room.checkIfBooked(FROM_DATE.plusDays(1), TO_DATE));
		assertTrue(room.checkIfBooked(FROM_DATE, TO_DATE.minusDays(1)));
	}

	@Test
	void checkIfBookedShouldReturnFalseIfDatesAreFree() {
		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//Then
		assertFalse(room.checkIfBooked(TO_DATE, TO_DATE.plusDays(5)));
		assertFalse(room.checkIfBooked(FROM_DATE.minusDays(5), FROM_DATE));
		assertFalse(room.checkIfBooked(TO_DATE.plusDays(2), TO_DATE.plusDays(5)));
		assertFalse(room.checkIfBooked(FROM_DATE.minusDays(5), FROM_DATE.minusDays(2)));
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnOnlySomeDates() {
		//Given
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//When
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE.plusDays(1), 1);

		//Then
		assertTrue(availableDates.contains(TO_DATE));
		assertTrue(availableDates.contains(TO_DATE.plusDays(1)));
		assertEquals(2, availableDates.size());
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnEmptyListIfNoDates() {
		//Given
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//When
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE.minusDays(1), 1);

		//Then
		assertTrue(availableDates.isEmpty());
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnEmptyListIfBedNotAvailable() {
		//Given

		//When
		room.createBooking(FROM_DATE, TO_DATE, EGN);

		//Then
		assertTrue(room.findAvailableDatesForIntervalAndSize(FROM_DATE,
			TO_DATE.plusDays(1), 2).isEmpty());
	}

	@Test
	void findAvailableDatesForIntervalAndSizeShouldReturnAllDatesIfNoBookings() {
		//When
		List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(FROM_DATE, TO_DATE, 1);
		List<LocalDate> allDates = FROM_DATE.datesUntil(TO_DATE).collect(Collectors.toList());
		allDates.add(TO_DATE);

		//Then
		assertThat("Not all of the dates were returned.", availableDates, equalTo(allDates));
	}

	@Test
	void setCommoditiesShouldThrowExcIfNullPassed() {
		assertThrows(NullCommodityException.class, () -> room.setCommodities(null));
	}

	@Test
	void setCommoditiesShouldWorkIfProperArgumentPassed() {
		assertDoesNotThrow(() -> room.setCommodities(new HashSet<>(Arrays.asList(new Shower(), new Bed(BedType.DOUBLE)))));
		assertTrue(room.checkIfEnoughBeds(2));
	}

	@Test
	void addCommodityShouldThrowExcIfNullPassed() {
		assertThrows(NullCommodityException.class, () -> room.addCommodity(null));
	}

	@Test
	void addCommodityShouldWorkIfProperArgumentPassed() {
		//Given
		Shower shower = new Shower();

		//When
		room.addCommodity(shower);

		//Then
		assertTrue(room.getCommodities().contains(shower));
	}

	@Test
	void addCommodityShouldIncrementCapacityIfBedPassed() {
		//Given
		Bed bed = new Bed(BedType.KING_SIZE);

		//When
		room.addCommodity(bed);

		//Then
		assertEquals(BedType.SINGLE.getSize() + BedType.KING_SIZE.getSize(), room.getCapacity());
	}

	@Test
	void checkIfEnoughBedsShouldReturnFalseIfNotEnough() {
		assertFalse(room.checkIfEnoughBeds(2));
	}

	@Test
	void checkIfEnoughBedsShouldReturnTrueIfNumberOfBedsEqualsRequired() {
		assertTrue(room.checkIfEnoughBeds(1));
	}

	@Test
	void hashCodeShouldBeDifferent() {
		//When
		Room room2 = new Room(ROOM_NUMBER + 1, new HashSet<>());

		//Then
		assertNotEquals(room.hashCode(), room2.hashCode());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingBooksWithSameHash() {
		//When
		Room room2 = room;

		//Then
		assertEquals(room, room2);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNullOrOtherType() {
		assertNotEquals(null, room);
		assertNotEquals("room", room);
	}
}
