package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.BookingActionException;
import eu.deltasource.internship.hotelcalifornia.utilities.HotelUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
	//Given
	private static final LocalDate FROM_DATE = LocalDate.of(2019, 9, 18);
	private static final LocalDate TO_DATE = LocalDate.of(2019, 9, 21);
	private static final long GUEST_ID = 1234567891L;
	private Booking booking = new Booking(FROM_DATE, TO_DATE, GUEST_ID);
	private LocalDate bookingToDate;
	private LocalDate bookingFromDate;

	@Test
	void constructorShouldCreateUniqueBookingNumber() {
		//when
		booking = new Booking(FROM_DATE, TO_DATE, GUEST_ID);
		Booking booking1 = new Booking(FROM_DATE, TO_DATE, GUEST_ID);

		//then
		assertThat("Booking numbers are the same", booking.getBookingNumber(), not(equalTo(booking1.getBookingNumber())));
	}

	@Test
	void constructorShouldCreateDefaultGuestNameIfNonePassed() {
		//Given
		booking = new Booking(FROM_DATE, TO_DATE, GUEST_ID);

		//When

		//Then
		assertEquals(HotelUtil.getUnknownName(), booking.getGuestName());
	}

	@Test
	void constructorShouldAssignPassedName() {
		//Given
		booking = new Booking(FROM_DATE, TO_DATE, "Ivan", GUEST_ID);

		//When

		//Then
		assertEquals("Ivan", booking.getGuestName());
	}


	@Test
	void setGuestIdShouldSetOnlyValidIds() {
		assertDoesNotThrow(() -> new Booking(FROM_DATE, TO_DATE, GUEST_ID));
	}

	@Test
	void setGuestIdShouldThrowExceptionIfIdNotValid() {
		assertThrows(BookingActionException.class, () -> new Booking(FROM_DATE, TO_DATE, GUEST_ID / 10));
	}

	@Test
	void changeReservationDatesShouldWorkIfDatesAreChronological() {
		//when
		booking.changeReservationDates(FROM_DATE, TO_DATE);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are not changed successfully", bookingFromDate, equalTo(FROM_DATE));
		assertThat("Dates are not changed successfully0,", bookingToDate, equalTo(TO_DATE));
	}

	@Test
	void changeReservationDatesShouldNotAssignNonChronologicalDates() {
		//when
		booking.changeReservationDates(TO_DATE, FROM_DATE);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are changed successfully", bookingToDate, not(equalTo(FROM_DATE)));
		assertThat("Dates are changed successfully,", bookingFromDate, not(equalTo(TO_DATE)));
	}

	@Test
	void changeReservationDatesShouldNotAssignNullDates() {
		//when
		booking.changeReservationDates(null, null);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Date is not null", bookingFromDate, not(equalTo(null)));
		assertThat("Date is not null", bookingToDate, not(equalTo(null)));
	}

	@Test
	void hashCodeShouldBeDifferent() {
		//when
		Booking booking2 = new Booking(FROM_DATE, TO_DATE, GUEST_ID);

		//then
		assertThat("Hash codes are the same!", booking.hashCode(), not(equalTo(booking2.hashCode())));
	}

	@Test
	void equalsShouldReturnTrueWhenComparingBookingsWithTheSameHash() {
		//when
		Booking booking2 = booking;

		//then
		assertThat("Bookings are not equal", booking, equalTo(booking2));
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNullOrOtherType() {
		assertThat("Comparing with null", booking, not(equalTo(null)));
		assertThat("Comparing with other type", booking, not(equalTo("booking")));
	}
}
