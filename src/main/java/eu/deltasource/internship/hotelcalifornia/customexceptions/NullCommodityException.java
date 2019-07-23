package eu.deltasource.internship.hotelcalifornia.customexceptions;

public class NullCommodityException extends InvalidHotelActionException {
	public NullCommodityException(String message) {
		super(message);
	}
}
