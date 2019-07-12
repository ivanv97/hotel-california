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
}
