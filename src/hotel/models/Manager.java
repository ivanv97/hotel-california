package hotel.models;

import customexceptions.InvalidChangeRoomStatusException;

public class Manager {
	private Hotel hotel;
	private String name;

	/**
	 * Only constructor of the Manager class
	 * The name should include both first and last name
	 *
	 * @param name full name of the manager
	 */
	public Manager(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Hotel getHotel() {
		return hotel;
	}

	/**
	 * Setter method which assigns a particular hotel
	 * to a manager (one to one relationship)
	 *
	 * @param hotel hotel instance parameter
	 */
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	/**
	 * Method which lets the manager choose
	 * a room by its number in order to book it
	 *
	 * @param number number of the room
	 * @throws InvalidChangeRoomStatusException Throws exception if the desired room is booked
	 *                                          or if there is no room with that number
	 */
	public void bookRoom(int number) throws InvalidChangeRoomStatusException {
		for (Room room : hotel.getRooms()) {
			if (room.getNumber() == number) {
				if (!room.changeRoomStatus(true)) {
					throw new InvalidChangeRoomStatusException("The room is already booked!");
				}
			}
		}
		throw new InvalidChangeRoomStatusException("No room with that number!");
	}

	/**
	 * Method which lets the manager check
	 * for free rooms by iterating through all
	 * of the rooms and book the first vacant one
	 *
	 * @throws InvalidChangeRoomStatusException throws custom exception if there are
	 *                                          no free rooms
	 */
	public void checkAndBookFirstFreeRoom() throws InvalidChangeRoomStatusException {
		for (Room room : hotel.getRooms()) {
			if (room.changeRoomStatus(true)) {
				return;
			}
		}
		throw new InvalidChangeRoomStatusException("There are no free rooms!");
	}

	/**
	 * Make all rooms available by calling
	 * the freeRoom method of each room object
	 */
	public void clearBookings() {
		for (Room room : hotel.getRooms()) {
			if (room.checkIfBooked()) {
				room.changeRoomStatus(false);
			}
		}
	}
}
