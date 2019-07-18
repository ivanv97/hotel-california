package hotel.models;

import customexceptions.InvalidHotelActionException;
import hotel.commodities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
	private static Hotel hotel;
	private static Manager manager;

	@BeforeAll
	static void setUp(){
		hotel = new Hotel("California", null);
		manager = new Manager("Ivan");
		manager.setHotel(hotel);
	}

	@AfterEach
	void tearDown(){
		hotel.getRooms().clear();
		hotel.getTakenRoomNumbers().clear();
		manager.getBookedRoomsNumbers().clear();
	}

	@Test
	void bookThreeRooms() {
		//given
		Set<AbstractCommodity> firstRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE), new Shower(), new Toilet()));

		Set<AbstractCommodity> secondRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
			new Bed(BedType.SINGLE), new Bed(BedType.SINGLE), new Shower(), new Toilet(), new Toilet()));

		Set<AbstractCommodity> thirdRoomCommodities = new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE),
			new Bed(BedType.SINGLE), new Toilet(), new Shower()));

		hotel.getRooms().add(new Room(101, firstRoomCommodities));
		hotel.getRooms().add(new Room(102, secondRoomCommodities));
		hotel.getRooms().add(new Room(103, thirdRoomCommodities));

		try {
			//when
			Map<Room, AvailableDatesList> availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 20),
				LocalDate.of(2019, 7, 25), 5, 2);

			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> manager.createBooking(LocalDate.of(2019, 7, 20),
					LocalDate.of(2019, 7, 25),2,1234567899L));
			}

			//when
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 22),
				LocalDate.of(2019, 7, 23), 1, 1);
			//then
			for (Room room : availableRoomsAndDates.keySet()) {
				assertDoesNotThrow(() -> manager.createBooking(LocalDate.of(2019, 7, 22),
					LocalDate.of(2019, 7, 23),1, 1234567898L));
				break;
			}

			//when
			availableRoomsAndDates = manager.getHotel().getFreeRoomsAndDates(
				LocalDate.of(2019, 7, 19),
				LocalDate.of(2019, 7, 21), 2, 2);
			//then
			for (Room room : availableRoomsAndDates.keySet()) {
				assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(LocalDate.of(2019, 7, 19),
					LocalDate.of(2019, 7, 21), 2,1234567898L));
				break;
			}

		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
	}

	@Test
	void createBookingsCustomTaskTestScenarioOne(){
		//Given
		Room room = new Room(101,new HashSet<>(Arrays.asList(new Bed(BedType.DOUBLE))));
		hotel.getRooms().add(room);

		//When
		int bookedRoomNumber = manager.createBooking(LocalDate.of(2019,1,1),
			LocalDate.of(2019,1,2),2,1234567891L);
		assertEquals(101,bookedRoomNumber);
	}

	@Test
	void createBookingsCustomTaskTestScenarioTwo(){
		assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(LocalDate.of(2019,1,1),
			LocalDate.of(2019,1,2),2,1234567891L));
	}

	@Test
	void createBookingsCustomTaskTestScenarioThree(){
		//Given
		Room room = new Room(101,new HashSet<>(Arrays.asList(new Bed(BedType.SINGLE))));
		hotel.getRooms().add(room);

		//Then
		assertThrows(InvalidHotelActionException.class, () -> manager.createBooking(LocalDate.of(2019,1,1),
			LocalDate.of(2019,1,2),2,1234567891L));
	}

}
