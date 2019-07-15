package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.AbstractCommodity;
import hotel.commodities.Bed;
import hotel.commodities.Shower;
import hotel.commodities.Toilet;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

	/**
	 * Creating three rooms with different types
	 * and number of commodities and try to create
	 * booking with different date intervals
	 * and size of room
	 */
	@Test
	void bookThreeRooms() {
		//one bed
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>();
		firstRoomCommodities.add(new Bed());
		firstRoomCommodities.add(new Shower());
		firstRoomCommodities.add(new Toilet());

		//three beds
		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>();
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Shower());
		secondRoomCommodities.add(new Toilet());
		secondRoomCommodities.add(new Toilet());

		//two beds
		Set<AbstractCommodity> thirdRoomCommodities = new HashSet<>();
		thirdRoomCommodities.add(new Bed());
		thirdRoomCommodities.add(new Bed());
		thirdRoomCommodities.add(new Toilet());
		thirdRoomCommodities.add(new Shower());

		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(new Room(101, firstRoomCommodities),
			new Room(102, secondRoomCommodities), new Room(103, thirdRoomCommodities))));
		Manager manager = new Manager("Ivan Velkushanov");
		manager.setHotel(hotel);

		try {
			Map<Room, List<LocalDate>> availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 20),
				LocalDate.of(2019, 7, 25), 5, 2);
			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> room.createBooking(LocalDate.of(2019, 7, 20),
					LocalDate.of(2019, 7, 25),"Ivan", 1234567898L));
				break;
			}

			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 22),
				LocalDate.of(2019, 7, 23), 1, 1);
			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> room.createBooking(LocalDate.of(2019, 7, 22),
					LocalDate.of(2019, 7, 23),"Ivan", 1234567898L));
				break;
			}

			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 19),
				LocalDate.of(2019, 7, 21), 2, 2);
			for (Room room : availableRoomsAndDates.keySet()) {
				assertThrows(InvalidHotelActionException.class, () -> room.createBooking(LocalDate.of(2019, 7, 19),
					LocalDate.of(2019, 7, 21),"Ivan", 1234567898L));
				break;
			}

		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}

}
