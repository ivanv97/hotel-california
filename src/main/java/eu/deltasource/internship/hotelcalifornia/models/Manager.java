package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;

import java.time.LocalDate;

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
	 * Creates booking if the room is not
	 * already booked and if the dates specified are
	 * in the correct order
	 *
	 * @param fromDate       book from
	 * @param toDate         book to
	 * @param numberOfPeople for how many people
	 * @param reserveeId     the EGN of the guest
	 * @return number of the booked room
	 * @throws InvalidHotelActionException the hotel method can
	 *                                     throw exception if there are no free rooms
	 */
	public int createBooking(LocalDate fromDate, LocalDate toDate, int numberOfPeople, long reserveeId) {
		int bookedRoomNumber = hotel.findAndBookFirstAvailableRoom(fromDate, toDate, numberOfPeople, reserveeId);
		return bookedRoomNumber;
	}

	/**
	 * Removes booking from the specified room
	 * Removes the room number from the bookedRoomsNumbers
	 *
	 * @param fromDate
	 * @param toDate
	 * @param roomNumber
	 * @return the number of the room if
	 * its booking was successfully removed
	 * @throws InvalidHotelActionException when a booking
	 *                                     was not found for the given arguments
	 */
	public int removeBooking(LocalDate fromDate, LocalDate toDate, int roomNumber) {
		for (Room room : hotel.getRooms()) {
			if (room.getNumber() == roomNumber) {
				room.removeBooking(fromDate, toDate);
				hotel.getBookedRoomsNumbers().remove(Integer.valueOf(roomNumber));
				return roomNumber;
			}
		}
		throw new InvalidHotelActionException("No such booking for the specified room number!");
	}
}
