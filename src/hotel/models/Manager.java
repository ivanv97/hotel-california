package hotel.models;

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
	 * @return <CODE>true</CODE>  if the room is successfully booked
	 * or <CODE>false</CODE> if the room number is invalid or room is occupied
	 */
	public boolean bookRoom(int number) {
		for (Room room : hotel.getRooms())
			if (room.getNumber() == number)
				return room.book();
		return false;
	}

	/**
	 * Method which lets the manager check
	 * for free rooms by iterating through all
	 * of the rooms and book the first vacant one
	 *
	 * @return <CODE>true</CODE> if there is a free room or
	 * <CODE>false</CODE> if there are no free rooms
	 */
	public boolean checkForFreeRooms() {
		for (Room room : hotel.getRooms())
			if (room.book())
				return true;
		return false;
	}

	/**
	 * Make all rooms available by calling
	 * the freeRoom method of each room object
	 */
	public void clearBookings() {
		for (Room room : hotel.getRooms())
			room.freeRoom();
	}

}
