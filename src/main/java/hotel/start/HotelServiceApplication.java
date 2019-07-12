package hotel.start;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.*;
import hotel.models.*;

import java.time.LocalDate;
import java.util.*;

public class HotelServiceApplication {
	public static void main(String[] args) {
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>();
		firstRoomCommodities.add(new Bed());
		firstRoomCommodities.add(new Shower());
		firstRoomCommodities.add(new Toilet());

		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>();
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Bed());
		secondRoomCommodities.add(new Shower());
		secondRoomCommodities.add(new Toilet());
		secondRoomCommodities.add(new Toilet());

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
			manager.getHotel().getFreeRoomsAndDates(LocalDate.of(2019, 07, 20),
				LocalDate.of(2019, 7, 25), 5, 2);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}
}

