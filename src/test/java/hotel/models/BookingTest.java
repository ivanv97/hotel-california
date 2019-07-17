package hotel.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BookingTest {
	private static final String NAME = "Ivan";
	private static final long EGN = 1234567891L;
	private static final LocalDate FROM_DATE = LocalDate.of(2019, 9, 18);
	private static final LocalDate TO_DATE = LocalDate.of(2019, 9, 21);
	private Booking booking = new Booking(FROM_DATE, TO_DATE, NAME, EGN);
	private LocalDate bookingToDate;
	private LocalDate bookingFromDate;

	@Test
	void constructorShouldCreateDefaultNameAndIdValuesIfInvalidPassed() {
		//when
		booking = new Booking(FROM_DATE, TO_DATE, null, 123456789L);

		//then
		assertThat("Guest name is known", booking.getGuestName(), equalTo("unknown"));
		assertThat("EGN is 10 digits long", booking.getGuestId(), equalTo(-1L));
	}

	@Test
	void constructorShouldAssignValuesIfValid() {
		//when
		booking = new Booking(FROM_DATE, TO_DATE, NAME, EGN);

		//then
		assertThat("Name is not assigned", booking.getGuestName(), equalTo(NAME));
		assertThat("EGN is not assigned", booking.getGuestId(), equalTo(EGN));
	}

	@Test
	void changeReservationDatesShouldWorkIfDatesAreChronological() {
		//when
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are not changed successfully", bookingToDate, equalTo(TO_DATE));
		assertThat("Dates are changed successfully0,", bookingFromDate, equalTo(FROM_DATE));
	}

	@Test
	void changeReservationDatesShouldFailIfDatesAreNotChronological() {
		//when
		booking = new Booking(TO_DATE, FROM_DATE, "Ivan", 1234567898L);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Dates are changed successfully", bookingToDate, not(equalTo(FROM_DATE)));
		assertThat("Dates are changed successfully0,", bookingFromDate, not(equalTo(TO_DATE)));
	}

	@Test
	void changeReservationDatesShouldFailIfDatesAreNull() {
		//when
		booking.changeReservationDates(null, null);
		bookingToDate = booking.getToDate();
		bookingFromDate = booking.getFromDate();

		//then
		assertThat("Date is null", bookingFromDate, not(equalTo(null)));
		assertThat("Date is null", bookingToDate, not(equalTo(null)));
	}

	@Test
	void hashCodeShouldBeDifferent() {
		//when
		Booking booking2 = new Booking(FROM_DATE, TO_DATE, "Peter", 1234567897L);

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
