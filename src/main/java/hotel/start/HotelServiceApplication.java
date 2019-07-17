package hotel.start;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.*;
import hotel.models.*;

import java.time.LocalDate;
import java.util.*;

public class HotelServiceApplication {
	public static void main(String[] args) {
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Shower(), new Toilet()));

		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Bed(), new Bed(),
			new Shower(), new Toilet(), new Toilet()));

		Set<AbstractCommodity> thirdRoomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Bed(), new Toilet(),
			new Shower()));

		Room room1 = new Room(101, firstRoomCommodities);
		Room room2 = new Room(102, secondRoomCommodities);
		Room room3 = new Room(103, thirdRoomCommodities);

		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room1, room2, room3)));
		Manager manager = new Manager("Ivan Velkushanov");
		manager.setHotel(hotel);

		Map<Room, AvailableDatesList> availableRoomsAndDates = null;
		try {
			room3.createBooking(LocalDate.of(2019, 07, 20),
				LocalDate.of(2019, 07, 22), "Ivan", 1234567895L);
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(LocalDate.of(2019, 07, 10),
				LocalDate.of(2019, 7, 25), 15, 2);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		for (Room room : availableRoomsAndDates.keySet()) {
			System.out.println("Room:\tDate:");
			for (LocalDate date : availableRoomsAndDates.get(room).getList()) {
				System.out.println(room + "\t\t" + date);
			}
		}
	}
}

