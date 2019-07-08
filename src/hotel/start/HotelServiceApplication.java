package hotel.start;

import hotel.models.*;

import java.util.ArrayList;
import java.util.Arrays;

public class HotelServiceApplication {
	public static void main(String[] args) {
		Manager manager = new Manager("Ivan Velkushanov");
		ArrayList<Room> rooms = new ArrayList<>(Arrays.asList(new Room(101), new Room(102)));
		Hotel california = new Hotel("California", rooms);
		manager.setHotel(california);
		manager.checkForFreeRooms();
		manager.clearBookings();
		System.out.println("The end!");
	}
}
