package hotel.models;


public class Room {
	private int number;
	private boolean isBooked;

	/**
	 * Only constructor of the Room class
	 * Call setter method for the room number
	 *
	 * @param number room number
	 */
	public Room(int number) {
		setNumber(number);
	}

	/**
	 * Checks if room with the same number is not
	 * already created, adds the number to the list
	 * of unavailable room numbers
	 *
	 * @param number number of the room to be set
	 */
	public void setNumber(int number) {
		if (!Hotel.getTakenRoomNumbers().contains(number)) {
			this.number = number;
			Hotel.getTakenRoomNumbers().add(number);
		}
	}

	public int getNumber() {
		return number;
	}

	/**
	 * Method which changes the status of the room
	 *
	 * @param isBooked boolean parameter - true if we want to book the room
	 *                 false if we want to free the room
	 * @return <CODE>true</CODE> if the action is successful
	 * <CODE>false</CODE> otherwise
	 */
	public boolean changeRoomStatus(boolean isBooked) {
		if (this.isBooked != isBooked) {
			this.isBooked = isBooked;
			return true;
		}
		return false;
	}

	/**
	 * Method which gets the status of the room
	 *
	 * @return <CODE>true</CODE> if the room is booked
	 * <CODE>false</CODE> if vacant
	 */
	public boolean checkIfBooked() {
		return isBooked;
	}
}
