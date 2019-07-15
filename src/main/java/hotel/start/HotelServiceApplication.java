package hotel.start;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.*;
import hotel.models.*;

import java.lang.reflect.Array;
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

		Room room1 = new Room(101, firstRoomCommodities);
		Room room2 = new Room(102, secondRoomCommodities);
		Room room3 = new Room(103, thirdRoomCommodities);

		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room1, room2, room3)));
		Manager manager = new Manager("Ivan Velkushanov");
		manager.setHotel(hotel);

		Map<Room, List<LocalDate>> availableRoomsAndDates = null;
		try {
			room3.createBooking(LocalDate.of(2019,07,20),
				LocalDate.of(2019,07,22),"Ivan",1234567895L);
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(LocalDate.of(2019, 07, 20),
				LocalDate.of(2019, 7, 25), 5, 2);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		System.out.println(availableRoomsAndDates);
	}
}

