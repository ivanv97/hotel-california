package hotel.models;

import java.util.ArrayList;

public class Hotel {
	private String name;
	private ArrayList<Room> rooms;

	/**
	 * Only constructor of the Hotel class
	 *
	 * @param name  <b>name</b> of the hotel
	 * @param rooms an array list consisting of all of the <b>rooms</b>
	 */
	public Hotel(String name, ArrayList<Room> rooms) {
		this.name = name;
		this.rooms = rooms;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method which returns an array list
	 * of all the <b>rooms</b>
	 *
	 * @return ArrayList of Rooms
	 */
	public ArrayList<Room> getRooms() {
		return rooms;
	}

}
