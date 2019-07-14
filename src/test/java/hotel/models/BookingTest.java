package hotel.models;

import customexceptions.InvalidHotelActionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

	/**
	 * Tests if we can instantiate a booking object
	 * Not allowed to if any of the parameters we pass is null
	 * or the EGN length is different than 10 digits
	 * Allowed when all values are valid
	 */
	@Test
	void constructorShouldNotRunWhenInvalidValuesPassed() {
		assertThrows(InvalidHotelActionException.class,
			() -> new Booking(null, null, null, 123456789));
		assertThrows(InvalidHotelActionException.class,
			() -> new Booking(null, LocalDate.of(2019, 9, 18),
				"Ivan", 1234567899));
		assertThrows(InvalidHotelActionException.class,
			() -> new Booking(LocalDate.of(2019, 9, 18), null,
				"Ivan", 1234567899));
		assertThrows(InvalidHotelActionException.class,
			() -> new Booking(LocalDate.of(2019, 9, 18), LocalDate.of(2019, 9, 21),
				null, 1234567899));
		assertThrows(InvalidHotelActionException.class,
			() -> new Booking(LocalDate.of(2019, 9, 18), LocalDate.of(2019, 9, 21),
				"Ivan", 123456789));
		assertDoesNotThrow(() -> new Booking(LocalDate.of(2019, 9, 18),
			LocalDate.of(2019, 9, 21), "Ivan", 1234567898L));
	}

	/**
	 * Test passes if reservatons dates are
	 * successfully changed when chronological
	 * First date is before the second
	 */
	@Test
	void changeReservationDatesShouldWorkIfDatesAreChronological() {
		LocalDate fromDate = LocalDate.of(2019, 9, 18);
		LocalDate toDate = LocalDate.of(2019, 9, 21);
		Booking booking = null;
		try {
			 booking = new Booking(fromDate, toDate, "Ivan", 1234567898L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertEquals(fromDate,booking.getFromDate());
		assertEquals(toDate, booking.getToDate());
	}

	/**
	 * Test passes if we are not allowed to change
	 * dates when not in chronological order or equal
	 */
	@Test
	void changeReservationDatesShouldFailIfDatesAreNotChronological() {
		LocalDate fromDate = LocalDate.of(2019, 9, 18);
		LocalDate toDate = LocalDate.of(2019, 9, 17);
		Booking booking = null;
		try {
			booking = new Booking(fromDate, toDate, "Ivan", 1234567898L);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		assertNotEquals(fromDate,booking.getFromDate());
		assertNotEquals(toDate, booking.getToDate());
	}

	/**
	 * Test passes when we have different rooms
	 * and their hashcodes are not the same
	 */
	@Test
	void hashCodeShouldBeDifferent() {
		try {
			Booking booking1 = new Booking(LocalDate.of(2019, 9, 18),
				LocalDate.of(2019, 9, 21), "Ivan", 1234567898L);
			Booking booking2 = new Booking(LocalDate.of(2019, 9, 25),
				LocalDate.of(2019, 9, 30), "Peter", 1234567897L);
			assertNotEquals(booking1.hashCode(), booking2.hashCode());
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Equals should return true when comparing
	 * identical objects - same hash value
	 */
	@Test
	void equalsShouldReturnTrueWhenComparingBookingsWithTheSameHash() {
		try {
			Booking booking1 = new Booking(LocalDate.of(2019, 9, 18),
				LocalDate.of(2019, 9, 21), "Ivan", 1234567898L);
			Booking booking2 = booking1;
			assertTrue(booking1.equals(booking2));
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Equals should return false if the we compare
	 * with null or object of different type
	 */
	@Test
	void equalsShouldReturnFalseWhenComparingWithNullOrOtherType(){
		try {
			Booking booking = new Booking(LocalDate.of(2019, 9, 18),
				LocalDate.of(2019, 9, 21), "Ivan", 1234567898L);
			assertFalse(booking.equals(null));
			assertFalse(booking.equals("booking"));
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}
}
