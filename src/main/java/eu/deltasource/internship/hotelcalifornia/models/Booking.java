package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.BookingActionException;

import java.time.LocalDate;

/**
 * This class serves as property
 * of each Room object - it allows
 * reservation dates to be updated and
 * it can be added to set
 *
 * @author Ivan Velkushanov
 */
public class Booking {
	private static final String UNKNOWN_NAME = "unknown";
	private static int lastNumberUsed;
	private final int bookingNumber;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String guestName;
	private long guestId;

	/**
	 * Constructor of the Booking class
	 * to be used when the name of the guest is
	 * unknown - calls overloaded version of the constructor
	 * which assigns "unknown" to guestName
	 *
	 * @param fromDate stay from
	 * @param toDate   stay to
	 * @param guestId  EGN
	 */
	public Booking(LocalDate fromDate, LocalDate toDate, long guestId) {
		this(fromDate, toDate, UNKNOWN_NAME, guestId);
	}

	/**
	 * Overloaded constructor that takes parameter
	 * guestName as well and assigns the argument passed
	 * to the private field guestName
	 *
	 * @param fromDate  stay from
	 * @param toDate    stay to
	 * @param guestName name of the guest
	 * @param guestId   EGN of the guest
	 */
	public Booking(LocalDate fromDate, LocalDate toDate, String guestName, long guestId) {
		this.guestName = guestName;
		changeReservationDates(fromDate, toDate);
		setGuestId(guestId);
		bookingNumber = ++lastNumberUsed;
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

	/**
	 * Set the guestId only if valid
	 * argument is passed
	 *
	 * @param guestId Valid EGN
	 */
	public void setGuestId(long guestId) {
		if (guestId / 1_000_000_000L >= 1) {
			this.guestId = guestId;
		} else {
			throw new BookingActionException("Invalid guest ID");
		}
	}

	public int getBookingNumber() {
		return bookingNumber;
	}

	/**
	 * Changes the reservation dates of the booking
	 * first checks if fromDate is before toDate
	 * and that they are not null
	 *
	 * @param fromDate the new beginning date
	 * @param toDate   the new final date
	 */
	public void changeReservationDates(LocalDate fromDate, LocalDate toDate) {
		if (fromDate != null && toDate != null) {
			if (toDate.isAfter(fromDate)) {
				this.fromDate = fromDate;
				this.toDate = toDate;
			}
		}
	}

	/**
	 * The hashCode method is overriden
	 * to return the booking's number which is unique
	 *
	 * @return bookingNumber
	 */
	@Override
	public int hashCode() {
		return bookingNumber;
	}

	/**
	 * Two booking objects are equal
	 * if their booking numbers and dates are equal
	 *
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
