package hotel.start;

import customexceptions.InvalidChangeRoomStatusException;
import hotel.models.*;

import java.util.ArrayList;
import java.util.Arrays;

public class HotelServiceApplication {
	public static void main(String[] args) {
		Manager manager = new Manager("Ivan Velkushanov");
		ArrayList<Room> rooms = new ArrayList<>(Arrays.asList(new Room(101), new Room(101)));
		Hotel california = new Hotel("California", rooms);
		manager.setHotel(california);
		try {
			manager.checkAndBookFirstFreeRoom();
		} catch (InvalidChangeRoomStatusException e) {
			System.out.println(e.getMessage());
		}
		manager.clearBookings();
		System.out.println("The end!");
	}
}
