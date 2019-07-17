package customexceptions;

/**
 * Exception which is thrown whenever
 * there is a problem with booking or
 * freeing of a room or finding
 * suitable dates for booking
 */
public class InvalidHotelActionException extends Exception {
	public InvalidHotelActionException(String message) {
		super(message);
	}
}
