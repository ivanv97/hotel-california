package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;

import java.time.LocalDate;
import java.util.*;

/**
 * Hotel class - consists of
 * name as list of Room objects
 * Implements logic for finding available rooms
 * for particular dates and number of people
 *
 * @author Ivan Velkushanov
 */
public class Hotel {
	private static final List<Integer> TAKEN_ROOM_NUMBERS = new ArrayList<>();
	private static final String UNKNOWN_NAME = "Unknown";
	private String name;
	private List<Room> rooms;

	public List<Integer> getBookedRoomsNumbers() {
		return bookedRoomsNumbers;
	}

	private List<Integer> bookedRoomsNumbers;

	/**
	 * Constructor with arguments
	 * for name of the hotel and rooms
	 *
	 * @param name  <b>name</b> of the hotel
	 * @param rooms an array list consisting of all of the <b>rooms</b>z
	 */
	public Hotel(String name, List<Room> rooms) {
		this.name = name;
		this.rooms = rooms;
		bookedRoomsNumbers = new ArrayList<>();
	}

	/**
	 * Calls the overloaded constructor with
	 * default values
	 */
	public Hotel() {
		this(UNKNOWN_NAME, new ArrayList<>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void addRoom(Room room) {
		for (Room currentRoom : rooms) {
			if (currentRoom.getNumber() == room.getNumber()) {
				throw new InvalidHotelActionException("Cannot have room with the same number");
			}
		}
		rooms.add(room);
	}

	public static List<Integer> getTakenRoomNumbers() {
		return TAKEN_ROOM_NUMBERS;
	}

	/**
	 * Gets a map of key value pairs -
	 * the room and the free dates for it
	 *
	 * @param fromDate     check from date
	 * @param toDate       check to date
	 * @param numberOfDays for number of days
	 * @param numberOfBeds for number of people
	 * @return returns map consisting only of rooms
	 * which are found to be vacant in at least one
	 * date of the specified interval
	 * @throws InvalidHotelActionException if the number of days does not
	 *                                     correspond with the specified interval
	 */
	public Map<Room, AvailableDatesList> getFreeRoomsAndDates(LocalDate fromDate, LocalDate toDate,
															  int numberOfDays, int numberOfBeds) {
		if (numberOfDays != (toDate.getDayOfYear() - fromDate.getDayOfYear())) {
			throw new InvalidHotelActionException("Number of days does not correspond with selected dates!");
		}
		Map<Room, AvailableDatesList> availableRooms = new HashMap<>();
		for (Room room : rooms) {
			List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(fromDate, toDate, numberOfBeds);
			availableRooms.put(room, new AvailableDatesList(availableDates));
		}
		return availableRooms;
	}

	/**
	 * Returns a list of the available rooms
	 * if such exists for the desired interval
	 * and number of people per room and is
	 * not already booked
	 *
	 * @param fromDate
	 * @param toDate
	 * @param numberOfPeople the number that should match the beds' capacity
	 * @return List of available rooms for the dates specified
	 * @throws InvalidHotelActionException when the specified interval does not offer any
	 *                                     free rooms thus the list remains empty
	 */
	public List<Room> findAvailableRooms(LocalDate fromDate, LocalDate toDate, int numberOfPeople) {
		List<Room> availableRooms = new ArrayList<>();
		if (toDate.isAfter(fromDate)) {
			for (Room room : rooms) {
				if (!bookedRoomsNumbers.contains(room.getNumber()) && !room.checkIfBooked(fromDate, toDate)) {
					if (room.checkIfEnoughBeds(numberOfPeople)) {
						availableRooms.add(room);
					}
				}
			}
		}
		if (availableRooms.isEmpty()) {
			throw new InvalidHotelActionException("There are no free rooms for the specified interval");
		}
		return availableRooms;
	}

	/**
	 * Finds all available rooms for the passed dates
	 * BOoks the first one and returns its number
	 *
	 * @param fromDate
	 * @param toDate
	 * @param numberOfPeople
	 * @param reserveeId
	 * @return the number of the successfully booked room
	 */
	public int findAndBookFirstAvailableRoom(LocalDate fromDate, LocalDate toDate, int numberOfPeople, long reserveeId) {
		List<Room> availableRooms = findAvailableRooms(fromDate, toDate, numberOfPeople);
		Room roomToBook = availableRooms.get(0);
		int bookedRoomNumber = roomToBook.createBooking(fromDate, toDate, reserveeId);
		bookedRoomsNumbers.add(bookedRoomNumber);
		return bookedRoomNumber;
	}
}
