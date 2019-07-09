package customexceptions;

public class InvalidChangeRoomStatusException extends Exception {
	public InvalidChangeRoomStatusException(String message) {
		super(message);
	}
}
