package hotel.models;

import java.util.ArrayList;

public class Room {
	private int number;
	private boolean isBooked;
	private static ArrayList<Integer> numbersTaken = new ArrayList<>();

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
		if (!numbersTaken.contains(number)) {
			this.number = number;
			numbersTaken.add(number);
		}
	}

	public int getNumber() {
		return number;
	}

	/**
	 * Books a room if it is not already booked
	 *
	 * @return <CODE>true</CODE> if the room is successfully booked
	 * or <CODE>false</CODE> if the room is already booked
	 */
	public boolean book() {
		if (isBooked)
			return false;
		isBooked = true;
		return true;
	}

	/**
	 * Clears the booking of the room
	 *
	 * @return <CODE>false</CODE> if the room is already vacant
	 * or <CODE>true</CODE> if the room booking is cleared
	 */
	public boolean freeRoom() {
		if (!isBooked)
			return false;
		isBooked = false;
		return true;
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
