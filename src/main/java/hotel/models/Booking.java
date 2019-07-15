package hotel.models;

import customexceptions.InvalidHotelActionException;

import java.time.LocalDate;

/**
 * This class serves as property
 * of each Room object - it allows
 * reservation dates to be updated and
 * it can be added to set
 * @author Ivan Velkushanov
 */
public class Booking {
	private static int lastNumberUsed;
	private final int bookingNumber;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String guestName;
	private final long guestId;

	/**
	 * Constructor of the Booking class
	 *
	 * @param fromDate
	 * @param toDate
	 * @param guestName
	 * @param guestId
	 * @throws InvalidHotelActionException throws custom exception if any of the
	 *                                     passed arguments is not valid for object creation
	 */
	public Booking(LocalDate fromDate, LocalDate toDate, String guestName, long guestId)
		throws InvalidHotelActionException {
		if (fromDate != null && toDate != null && guestName != null && guestId/1_000_000_000L >= 1) {
			changeReservationDates(fromDate, toDate);
			this.guestName = guestName;
			this.guestId = guestId;
			bookingNumber = ++lastNumberUsed;
		} else {
			throw new InvalidHotelActionException("Booking object cannot be created!");
		}
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public long getGuestId() {
		return guestId;
	}

	public int getBookingNumber() {
		return bookingNumber;
	}

	/**
	 * Changes the reservation dates of the booking
	 * first checks if fromDate is before toDate
	 *
	 * @param fromDate the new beginning date
	 * @param toDate   the new final date
	 */
	public void changeReservationDates(LocalDate fromDate, LocalDate toDate) {
		if (!toDate.isBefore(fromDate)) {
			this.fromDate = fromDate;
			this.toDate = toDate;
		}
	}

	/**
	 * The hashCode method is overriden
	 * to return the booking's number which is unique
	 * @return bookingNumber
	 */
	@Override
	public int hashCode() {
		return bookingNumber;
	}

	/**
	 * Two booking objects are equal
	 * if their booking numbers and dates are equal
	 * @param object booking object to compare with
	 * @return true - if the bookings are equal
	 * false if comparing with null or other type
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Booking)) {
			return false;
		}
		Booking booking = (Booking) object;
		return this.bookingNumber == booking.getBookingNumber()
			&& this.fromDate.equals(booking.getFromDate())
			&& this.toDate.equals(booking.getToDate());
	}
}
