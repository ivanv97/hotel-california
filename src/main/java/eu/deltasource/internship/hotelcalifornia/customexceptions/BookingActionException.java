package eu.deltasource.internship.hotelcalifornia.customexceptions;

public class BookingActionException extends InvalidHotelActionException {
	public BookingActionException(String message) {
		super(message);
	}
}