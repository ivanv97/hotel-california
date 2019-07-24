package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.commodities.AbstractCommodity;
import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;
import eu.deltasource.internship.hotelcalifornia.customexceptions.NullCommodityException;

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
	private static final Map<AbstractCommodity, Room> COMMODITY_ROOM_MAP = new HashMap<>();
	private static final Set<AbstractCommodity> AVAILABLE_COMMODITIES_SET = new HashSet<>();
	private static final String UNKNOWN_NAME = "unknown";
	private String name;
	private List<Room> rooms;

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

	public static Map<AbstractCommodity, Room> getCommodityRoomMap() {
		return COMMODITY_ROOM_MAP;
	}

	public static Set<AbstractCommodity> getAvailableCommoditiesSet() {
		return AVAILABLE_COMMODITIES_SET;
	}

	/**
	 * Add new room to the hotel -
	 * if we try to add the same room
	 * the method fails
	 *
	 * @param room the new room to be added
	 * @throws InvalidHotelActionException when we try to add
	 *                                     the same room twice
	 */
	public void addRoom(Room room) {
		for (Room currentRoom : rooms) {
			if (currentRoom.equals(room)) {
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
		Map<Room, AvailableDatesList> availableRoomsAndDates = new HashMap<>();
		for (Room room : rooms) {
			List<LocalDate> availableDates = room.findAvailableDatesForIntervalAndSize(fromDate, toDate, numberOfBeds);
			if (!availableDates.isEmpty()) {
				availableRoomsAndDates.put(room, new AvailableDatesList(availableDates));
			}
		}
		if (availableRoomsAndDates.isEmpty()) {
			throw new InvalidHotelActionException("No rooms and dates were found for the desired specifications.");
		}
		return availableRoomsAndDates;
	}

	/**
	 * Returns a list of the available rooms
	 * if such exists for the desired interval
	 * and number of people per room and is
	 * not already booked
	 *
	 * @param fromDate       start of the interval
	 * @param toDate         end of the interval
	 * @param numberOfPeople the number that should match the beds' capacity
	 * @return List of available rooms for the dates specified
	 * @throws InvalidHotelActionException when the specified interval does not offer any
	 *                                     free rooms thus the list remains empty
	 */
	public List<Room> findAvailableRooms(LocalDate fromDate, LocalDate toDate, int numberOfPeople) {
		List<Room> availableRooms = new ArrayList<>();
		if (toDate.isAfter(fromDate)) {
			for (Room room : rooms) {
				if (!room.checkIfBooked(fromDate, toDate)) {
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
	 * @param fromDate       book from
	 * @param toDate         book to
	 * @param numberOfPeople for capacity
	 * @param reserveeId     guestID
	 * @return the number of the successfully booked room
	 */
	public int findAndBookFirstAvailableRoom(LocalDate fromDate, LocalDate toDate, int numberOfPeople, long reserveeId) {
		List<Room> availableRooms = findAvailableRooms(fromDate, toDate, numberOfPeople);
		Room roomToBook = availableRooms.get(0);
		return roomToBook.createBooking(fromDate, toDate, reserveeId);
	}
}
