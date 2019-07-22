package hotel.models;

import customexceptions.InvalidHotelActionException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
	//Given
	private static final LocalDate FROM_DATE = LocalDate.of(2019, 9, 18);
	private static final LocalDate TO_DATE = LocalDate.of(2019, 9, 21);
	private static final long EGN = 1234567891L;
	private Booking booking = new Booking(FROM_DATE, TO_DATE, EGN);
	private LocalDate bookingToDate;
	private LocalDate bookingFromDate;

	@Test
	void constructorShouldCreateUniqueBookingNumber() {
		//when
		booking = new Booking(FROM_DATE, TO_DATE, EGN);
		Booking booking1 = new Booking(FROM_DATE, TO_DATE, EGN);

		//then
		assertThat("Booking numbers are the same", booking.getBookingNumber(), not(equalTo(booking1.getBookingNumber())));
	}

	@Test
	void setGuestIdShouldSetOnlyValidIds(){
		assertDoesNotThrow(() ->new Booking(FROM_DATE, TO_DATE, EGN));
	}

	@Test
	void setGuestIdShouldThrowExceptionWhenIdNotValid(){
		assertThrows(InvalidHotelActionException.class,() ->new Booking(FROM_DATE, TO_DATE, EGN/10));
	}

	@Test
	void changeReservationDatesShouldWorkIfDatesAreChronological() {
		//when
		booking.changeReservationDates(FROM_DATE,TO_DATE);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are not changed successfully", bookingFromDate, equalTo(FROM_DATE));
		assertThat("Dates are not changed successfully0,", bookingToDate, equalTo(TO_DATE));
	}

	@Test
	void changeReservationDatesShouldFailIfDatesAreNotChronological() {
		//when
		booking.changeReservationDates(TO_DATE,FROM_DATE);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are changed successfully", bookingToDate, not(equalTo(FROM_DATE)));
		assertThat("Dates are changed successfully,", bookingFromDate, not(equalTo(TO_DATE)));
	}

	@Test
	void changeReservationDatesShouldFailIfDatesAreNull() {
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
		Booking booking2 = new Booking(FROM_DATE, TO_DATE, 1234567897L);

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
