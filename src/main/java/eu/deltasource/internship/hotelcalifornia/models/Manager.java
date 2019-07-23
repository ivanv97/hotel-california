package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.BookingActionException;
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
	 * @throws InvalidHotelActionException if the argument
	 *                                     passed is null
	 */
	public void setHotel(Hotel hotel) {
		if (hotel == null) {
			throw new InvalidHotelActionException("Hotel set to manager cannot be null");
		}
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
		return hotel.findAndBookFirstAvailableRoom(fromDate, toDate, numberOfPeople, reserveeId);
	}

	/**
	 * Identifies a booking by its start and end date and
	 * removes the booking from the specified room
	 *
	 * @param fromDate   booking with this fromDate
	 * @param toDate     booking with this toDate
	 * @param roomNumber booking should be in this room
	 * @return the number of the room if
	 * its booking was successfully removed
	 * @throws BookingActionException when a booking
	 *                                was not found for the given arguments
	 */
	public int removeBooking(LocalDate fromDate, LocalDate toDate, int roomNumber) {
		for (Room room : hotel.getRooms()) {
			if (room.getNumber() == roomNumber) {
				room.removeBooking(fromDate, toDate);
				return roomNumber;
			}
		}
		throw new BookingActionException("No such booking for the specified room number!");
	}
}
