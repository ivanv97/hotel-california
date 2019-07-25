package eu.deltasource.internship.hotelcalifornia.services;

/**
 * Service class used for
 * getting a unique booking number
 */
public class BookingService {
	private static int lastNumberUsed;

	public static int getBookingNumber() {
		return ++lastNumberUsed;
	}
}
