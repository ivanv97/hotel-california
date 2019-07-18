package hotel.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class - has access to Hotel
 * which in turn takes care of finding
 * available rooms and dates
 *
 * @author Ivan Velkushanov
 */
public class Manager {
	private Hotel hotel;
	private String name;
	private List<Integer> bookedRoomsNumbers = new ArrayList<>();

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

	public List<Integer> getBookedRoomsNumbers() {
		return bookedRoomsNumbers;
	}

	/**
	 * Creates booking if the room is not
	 * already booked and if the dates specified are
	 * in the correct order
	 * @param fromDate book from
	 * @param toDate book to
	 * @param numberOfPeople for how many people
	 * @param reserveeId the EGN of the guest
	 * @return number of the booked room
	 * @throws customexceptions.InvalidHotelActionException the hotel method can
	 * throw exception if there are no free rooms
	 */
	public int createBooking(LocalDate fromDate, LocalDate toDate, int numberOfPeople, long reserveeId){
		List<Room> availableRooms = hotel.findAvailableRooms(fromDate,toDate, numberOfPeople, bookedRoomsNumbers);
		int bookedRoomNumber = availableRooms.get(0).createBooking(fromDate,toDate,reserveeId);
		bookedRoomsNumbers.add(bookedRoomNumber);
		return bookedRoomNumber;
	}
}
