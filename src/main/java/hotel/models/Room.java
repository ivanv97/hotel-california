package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.AbstractCommodity;
import hotel.commodities.Bed;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Room {
	private int number;
	private Set<AbstractCommodity> commodities;
	private Set<LocalDate> maintenanceDates;
	private Set<Booking> bookings;

	/**
	 * Only constructor of the Room class
	 * Call setter method for the room number
	 * Initializes the maintenanceDates and bookings hashsets
	 *
	 * @param number      room number
	 * @param commodities The commodities belonging to the room
	 */
	public Room(int number, Set<AbstractCommodity> commodities) {
		if (commodities == null) {
			this.commodities = new HashSet<>();
		} else {
			this.commodities = commodities;
		}
		setNumber(number);
		maintenanceDates = new HashSet<>();
		bookings = new HashSet<>();
	}

	public Set<AbstractCommodity> getCommodities() {
		return commodities;
	}

	public Set<Booking> getBookings() {
		return bookings;
	}

	public Set<LocalDate> getMaintenanceDates() {
		return maintenanceDates;
	}

	/**
	 * Checks if room with the same number is not
	 * already created, adds the number to the list
	 * of unavailable room numbers
	 *
	 * @param number number of the room to be set
	 */
	public void setNumber(int number) {
		if (!Hotel.getTakenRoomNumbers().contains(number)) {
			this.number = number;
			Hotel.getTakenRoomNumbers().add(number);
		}
	}

	public int getNumber() {
		return number;
	}

	/**
	 * Prepares the commodities -
	 * adds a maintenance date when this happens
	 *
	 * @param date the date on which the maintenance of
	 *             the room takes place
	 */
	public void prepareCommodities(LocalDate date) {
		for (AbstractCommodity commodity : commodities) {
			commodity.prepare();
		}
		maintenanceDates.add(date);
	}

	/**
	 * Creates a booking of the respective room object
	 * Also calls the prepareCommodities method for the date
	 * on which the guests are to be expected
	 *
	 * @param fromDate  arrival date
	 * @param toDate    departing date
	 * @param guestName name of the guest
	 * @param guestId   EGN of the guest
	 * @throws InvalidHotelActionException if the specified interval intervenes with
	 *                                     already created bookings
	 */
	public void createBooking(LocalDate fromDate, LocalDate toDate, String guestName, long guestId)
		throws InvalidHotelActionException {
		if (!checkIfBooked(fromDate, toDate)) {
			bookings.add(new Booking(fromDate, toDate, guestName, guestId));
			prepareCommodities(fromDate);
			return;
		}
		throw new InvalidHotelActionException("Booking can't be made with the specified date interval!");
	}


	/**
	 * Removes booking from the hashset if the specified arguments
	 * correspond with existing booking
	 * Prepares commodities after the guests leave
	 *
	 * @param fromDate from date
	 * @param toDate   to date
	 * @throws InvalidHotelActionException if a booking with the specified interval
	 *                                     does not exists
	 */
	public void removeBooking(LocalDate fromDate, LocalDate toDate) throws InvalidHotelActionException {
		for (Booking booking : bookings) {
			if (booking.getFromDate().equals(fromDate) && booking.getToDate().equals(toDate)) {
				bookings.remove(booking);
				prepareCommodities(toDate);
				return;
			}
		}
		throw new InvalidHotelActionException("No such booking exists!");
	}

	/**
	 * Checks if room is booked
	 * for a specified interval
	 *
	 * @param fromDate
	 * @param toDate
	 * @return true if it is booked
	 * false if it is vacant
	 */
	public boolean checkIfBooked(LocalDate fromDate, LocalDate toDate) {
		if (bookings.isEmpty()) {
			return false;
		}
		//If the from date is within a booking
		//Or if the to date is within a booking
		//Or if the from or to date coincide with a booking's dates
		for (Booking booking : bookings) {
			if ((fromDate.isAfter(booking.getFromDate()) && fromDate.isBefore(booking.getToDate()))
				|| (toDate.isAfter(booking.getFromDate()) && toDate.isBefore((booking.getToDate())))
				|| (fromDate.equals(booking.getFromDate()) || toDate.equals((booking.getToDate())))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the list of available dates of a room
	 *
	 * @param fromDate     check from date
	 * @param toDate       check to date
	 * @param numberOfBeds how many beds in the room
	 * @return the list of the dates - can be empty if no available dates
	 * found
	 */

	public List<LocalDate> findAvailableDatesForIntervalAndSize(LocalDate fromDate, LocalDate toDate, int numberOfBeds) {
		int currentNumberOfBeds = 0;
		//Get the number of beds in the room
		for (AbstractCommodity commodity : commodities) {
			if (commodity instanceof Bed) {
				currentNumberOfBeds++;
			}
		}
		//Return empty array list if the number of beds does not match
		if (currentNumberOfBeds != numberOfBeds) {
			return new ArrayList<>();
		}

		//If there are no bookings for the room
		//Return all of the dates in the specified interval
		if (bookings.isEmpty()) {
			List<LocalDate> allDates = fromDate.datesUntil(toDate).collect(Collectors.toList());
			allDates.add(toDate);
			return allDates;
		}

		//Iterate through all of the dates in the desired interval
		//Add or remove it to the final list if it
		//Satisfies the condition - is after or equal to the booking final date
		//Is before the booking start date
		ArrayList<LocalDate> availableDates = new ArrayList<>();
		for (Booking booking : bookings) {
			for (LocalDate currentDate = fromDate; !currentDate.isAfter(toDate); currentDate = currentDate.plusDays(1)) {
				if (currentDate.equals(booking.getToDate()) || currentDate.isAfter(booking.getToDate())
					|| currentDate.isBefore(booking.getFromDate())) {
					availableDates.add(currentDate);
				} else {
					availableDates.remove(currentDate);
				}
			}
		}
		return availableDates;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Room)) {
			return false;
		}
		Room room = (Room) object;
		return this.number == room.getNumber();
	}

	@Override
	public String toString(){
		return Integer.toString(number);
	}
}
