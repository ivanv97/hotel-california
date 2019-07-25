package eu.deltasource.internship.hotelcalifornia.services;

import eu.deltasource.internship.hotelcalifornia.commodities.AbstractCommodity;
import eu.deltasource.internship.hotelcalifornia.models.Room;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class which helps dealing
 * with room numbers being unique,
 * tracking which commodity goes where
 */
public final class HotelService {
	private static final List<Integer> TAKEN_ROOM_NUMBERS = new ArrayList<>();
	private static final Map<AbstractCommodity, Room> COMMODITY_ROOM_MAP = new HashMap<>();
	private static final Set<AbstractCommodity> AVAILABLE_COMMODITIES_SET = new HashSet<>();

	public static Set<AbstractCommodity> getAvailableCommoditiesSet() {
		return AVAILABLE_COMMODITIES_SET;
	}

	public static Map<AbstractCommodity, Room> getCommodityRoomMap() {
		return COMMODITY_ROOM_MAP;
	}

	public static List<Integer> getTakenRoomNumbers() {
		return TAKEN_ROOM_NUMBERS;
	}

	/**
	 * Checks if the suggested room number
	 * is free and returns it if so - else
	 * it returns 0
	 *
	 * @param roomNumber suggested number for the room
	 * @return 0 if the number is not applicable or the
	 * number if it is valid
	 */
	public static int getRoomNumberIfValid(int roomNumber) {
		if (!TAKEN_ROOM_NUMBERS.contains(roomNumber)) {
			TAKEN_ROOM_NUMBERS.add(roomNumber);
			return roomNumber;
		}
		return 0;
	}

	/**
	 * Removes commodity from the map
	 * and adds it to the available commodities set
	 *
	 * @param commodity the commodity to be made vacant
	 */
	public static void makeCommodityAvailable(AbstractCommodity commodity) {
		COMMODITY_ROOM_MAP.remove(commodity);
		AVAILABLE_COMMODITIES_SET.add(commodity);
	}

	/**
	 * Gets the room object
	 * which the commodity belongs to
	 *
	 * @param commodity search this commodity
	 * @return room where the commodity is located
	 */
	public static Room getCommodityLocation(AbstractCommodity commodity) {
		return COMMODITY_ROOM_MAP.get(commodity);
	}

	/**
	 * Puts the commodity to the new location
	 * and removes it from the set of available ones
	 *
	 * @param commodity the commodity to add
	 * @param room      where to add it
	 */
	public static void addCommodityToRoom(AbstractCommodity commodity, Room room) {
		COMMODITY_ROOM_MAP.put(commodity, room);
		AVAILABLE_COMMODITIES_SET.remove(commodity);
	}

	/**
	 * Prepares commodities to be set to
	 * a room - removes the commodities which
	 * are to be set from the map and the rooms they belong to
	 * (if they do)
	 * Removes the current commodities from the room and
	 * makes them available
	 *
	 * @param commodities commodities to be set
	 * @param room        the room to set them in
	 */
	public static void prepareCommoditiesForRoom(Set<AbstractCommodity> commodities, Room room) {
		for (AbstractCommodity commodity : commodities) {
			Room currentRoom = COMMODITY_ROOM_MAP.get(commodity);
			if (currentRoom != null) {
				currentRoom.getCommodities().remove(commodity);
			}
			COMMODITY_ROOM_MAP.remove(commodity);
		}
		for (AbstractCommodity commodity : room.getCommodities()) {
			COMMODITY_ROOM_MAP.remove(commodity);
			AVAILABLE_COMMODITIES_SET.add(commodity);
		}
	}

	/**
	 * Method which checks if to date
	 * is after from date and none of the
	 * dates is null
	 *
	 * @param fromDate from date to check
	 * @param toDate   to date to check
	 * @return false if dates are null or not chronological,
	 * true if from date is before to date
	 */
	public static boolean checkIfDatesChronological(LocalDate fromDate, LocalDate toDate) {
		if (fromDate == null || toDate == null) {
			return false;
		}
		return toDate.isAfter(fromDate);
	}
}
