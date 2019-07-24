package eu.deltasource.internship.hotelcalifornia.models;

import eu.deltasource.internship.hotelcalifornia.customexceptions.BookingActionException;
import eu.deltasource.internship.hotelcalifornia.customexceptions.InvalidHotelActionException;
import eu.deltasource.internship.hotelcalifornia.commodities.AbstractCommodity;
import eu.deltasource.internship.hotelcalifornia.commodities.Bed;
import eu.deltasource.internship.hotelcalifornia.customexceptions.NullCommodityException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Room class - provides basis
 * for creating a room with commodities,
 * dates on which it was under maintenance
 * and current bookings
 *
 * @author Ivan Velkushanov
 */
public class Room {
	private int number;
	private Set<AbstractCommodity> commodities;
	private Set<LocalDate> maintenanceDates;
	private Set<Booking> bookings;
	private int capacity;

	/**
	 * Only constructor of the Room class
	 * Call setter method for the commodities
	 * Call setter method for the room number
	 * Initializes the maintenanceDates and bookings hash sets
	 *
	 * @param number      room number
	 * @param commodities The commodities belonging to the room
	 */
	public Room(int number, Set<AbstractCommodity> commodities) {
		setCommodities(commodities);
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

	public int getCapacity() {
		return capacity;
	}

	/**
	 * Initializes the hash map of commodities
	 * if it is not already
	 * First removes the passed set of commodities
	 * from the hash map in the hotel
	 * in case any of it belong to another room
	 * Removes the current commodities
	 * of the room from the hash map in the Hotel
	 * and from the set of the room object
	 * and then adds the new commodities one by one
	 *
	 * @param commodities the set of commodities to be assigned
	 * @throws NullCommodityException if the commodities passed
	 *                                as argument are null
	 */
	public void setCommodities(Set<AbstractCommodity> commodities) {
		if (commodities == null) {
			throw new NullCommodityException("Cannot set 0 commodities to a room");
		}
		if (this.commodities == null) {
			this.commodities = new HashSet<>();
		}
		for (AbstractCommodity commodity : commodities) {
			Hotel.getCommodityRoomMap().remove(commodity);
		}
		for (AbstractCommodity commodity : this.commodities) {
			Hotel.getCommodityRoomMap().remove(commodity);
		}
		this.commodities.clear();
		this.capacity = 0;
		for (AbstractCommodity commodity : commodities) {
			addCommodity(commodity);
		}
	}

	/**
	 * If the commodity already belongs to the
	 * room - does nothing. Else, it frees up
	 * the commodity (removes it from commodityRoomMap)
	 * and adds commodity to the existing set
	 * and respectively to the map of the hotel class
	 * with object value being the current room
	 * If commodity happens to be bed -
	 * it increments the capacity by the
	 * bed size. Removes commodity from the set of available ones.
	 *
	 * @param commodity the commodity to be added to the set
	 * @throws NullCommodityException When trying
	 *                                to pass a null reference
	 */
	public void addCommodity(AbstractCommodity commodity) {
		if (commodity == null) {
			throw new NullCommodityException("Cannot add null commodity");
		}
		if (Hotel.getCommodityRoomMap().containsKey(commodity)) {
			Room currentRoom = Hotel.getCommodityRoomMap().get(commodity);
			if (currentRoom != null && currentRoom.equals(this)) {
				return;
			}
		}
		Hotel.getCommodityRoomMap().remove(commodity);
		this.commodities.add(commodity);
		if (commodity instanceof Bed) {
			Bed bed = (Bed) commodity;
			capacity += bed.getBedType().getSize();
		}
		Hotel.getCommodityRoomMap().put(commodity, this);
		Hotel.getAvailableCommoditiesSet().remove(commodity);
	}

	/**
	 * Removes commodity from the existing
	 * set and the hash map in the hotel
	 * and decreases the capacity of the
	 * room if the commodity is bed
	 * Adds commodity to the set of available ones.
	 *
	 * @param commodity the commodity to be removed
	 * @throws NullCommodityException when trying to pass
	 *                                null reference
	 */
	public void removeCommodity(AbstractCommodity commodity) {
		if (commodity == null) {
			throw new NullCommodityException("Cannot remove non-existing commodity");
		}
		if (this.commodities.contains(commodity)) {
			this.commodities.remove(commodity);
			if (commodity instanceof Bed) {
				Bed bed = (Bed) commodity;
				capacity -= bed.getBedType().getSize();
			}
			Hotel.getCommodityRoomMap().remove(commodity);
			Hotel.getAvailableCommoditiesSet().add(commodity);
		}
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
	 * on which the guests are to be expected - Should NOT
	 * be called directly but through the Hotel class!
	 *
	 * @param fromDate arrival date
	 * @param toDate   departing date
	 * @param guestId  EGN of the guest
	 */
	public int createBooking(LocalDate fromDate, LocalDate toDate, long guestId) {
		bookings.add(new Booking(fromDate, toDate, guestId));
		prepareCommodities(fromDate);
		return number;
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
	public void removeBooking(LocalDate fromDate, LocalDate toDate) {
		for (Booking booking : bookings) {
			if (booking.getFromDate().equals(fromDate) && booking.getToDate().equals(toDate)) {
				bookings.remove(booking);
				prepareCommodities(toDate);
				return;
			}
		}
		throw new BookingActionException("Can't remove booking! No such booking exists!");
	}

	/**
	 * Checks if room is booked
	 * for a specified interval
	 * It' considered booked if the from or to date
	 * is within an existing booking or if the two
	 * dates coincide with those of a booking
	 *
	 * @param fromDate date the stay starts
	 * @param toDate   date the stay ends
	 * @return true if it is booked
	 * false if it is vacant
	 * false if there are no bookings
	 */
	public boolean checkIfBooked(LocalDate fromDate, LocalDate toDate) {
		if (bookings.isEmpty()) {
			return false;
		}

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
	 * If no bookings - returns all dates
	 * Adds or removes the date if it belongs
	 * to the interval - so we have a proper list
	 * in the end
	 *
	 * @param fromDate check from date
	 * @param toDate   check to date
	 * @param capacity bed capacity in the room
	 * @return the list of the dates - can be empty if no available dates
	 * found or the number of beds does not correspond to the wanted one
	 */
	public List<LocalDate> findAvailableDatesForIntervalAndSize(LocalDate fromDate, LocalDate toDate, int capacity) {
		if (!checkIfEnoughBeds(capacity)) {
			return new ArrayList<>();
		}

		if (bookings.isEmpty()) {
			List<LocalDate> allDates = fromDate.datesUntil(toDate).collect(Collectors.toList());
			allDates.add(toDate);
			return allDates;
		}

		List<LocalDate> availableDates = new ArrayList<>();
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

	/**
	 * Checks if the requested number of
	 * beds matches the actual number of
	 * beds in the room
	 *
	 * @param capacity requested bed capacity
	 * @return true - if the numbers match
	 * false - if they don't
	 */
	public boolean checkIfEnoughBeds(int capacity) {
		return this.capacity == capacity;
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
	public String toString() {
		return Integer.toString(number);
	}
}
