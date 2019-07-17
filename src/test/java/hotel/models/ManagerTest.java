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

	@Test
	void bookThreeRooms() {
		//given
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Shower(), new Toilet()));

		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Bed(), new Bed(),
			new Shower(), new Toilet(), new Toilet()));

		Set<AbstractCommodity> thirdRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Bed(), new Toilet(),
			new Shower()));

		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(new Room(101, firstRoomCommodities),
			new Room(102, secondRoomCommodities), new Room(103, thirdRoomCommodities))));
		Manager manager = new Manager("Ivan Velkushanov");
		manager.setHotel(hotel);

		try {
			//when
			Map<Room, AvailableDatesList> availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 20),
				LocalDate.of(2019, 7, 25), 5, 2);
			//then
			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> room.createBooking(LocalDate.of(2019, 7, 20),
					LocalDate.of(2019, 7, 25), "Ivan", 1234567898L));
				break;
			}

			//when
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 22),
				LocalDate.of(2019, 7, 23), 1, 1);
			//then
			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> room.createBooking(LocalDate.of(2019, 7, 22),
					LocalDate.of(2019, 7, 23), "Ivan", 1234567898L));
				break;
			}

			//when
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 19),
				LocalDate.of(2019, 7, 21), 2, 2);
			//then
			for (Room room : availableRoomsAndDates.keySet()) {
				assertThrows(InvalidHotelActionException.class, () -> room.createBooking(LocalDate.of(2019, 7, 19),
					LocalDate.of(2019, 7, 21), "Ivan", 1234567898L));
				break;
			}

		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}

}
