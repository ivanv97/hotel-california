package hotel.models;

import customexceptions.InvalidChangeRoomStatusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
	Hotel hotel;
	Manager manager;

	/**
	 * For the test precondition we create a hotel, manager and two rooms
	 * with different numbers
	 */
	@BeforeEach
	void setUp() {
		hotel = new Hotel("California",
			new ArrayList<>(Arrays.asList(new Room(101), new Room(102))));
		manager = new Manager("Ivan Velkushanov");
		manager.setHotel(hotel);
	}

	@AfterEach
	void tearDown() {
		hotel.getTakenRoomNumbers().clear();
	}

	/**
	 * Test passes if we are not allowed to book
	 * a non-existing room
	 */
	@Test
	void bookRoomShouldThrowExceptionIfNoRoomWithThisNumber() {
		assertThrows(InvalidChangeRoomStatusException.class, () -> manager.bookRoom(202));
	}

	/**
	 * Test passes if we are not allowed to book
	 * an already booked room
	 */
	@Test
	void bookRoomShouldThrowExceptionIfTheRoomIsBooked() {
		manager.getHotel().getRooms().get(1).changeRoomStatus(true);
		assertThrows(InvalidChangeRoomStatusException.class, () -> manager.bookRoom(102));
	}

	/**
	 * Test passes if we manage to book a vacant
	 * and existing room
	 */
	@Test
	void bookRoomShouldBookTheRoomIfVacantAndExisting() {
		assertDoesNotThrow(() -> manager.bookRoom(101));
	}

	/**
	 * Test passes if the manager succeeds to find
	 * a vacant room and books it
	 */
	@Test
	void checkAndBookFirstFreeRoomShouldSucceedIfVacantRoomFound() {
		assertDoesNotThrow(() -> manager.checkAndBookFirstFreeRoom());
	}

	/**
	 * Test passes if we are not allowed to book anything
	 * when there are no vacant rooms
	 */
	@Test
	void checkAndBookFirstFreeRoomShouldNotSucceedIfNoVacantRoomFound() {
		try {
			manager.bookRoom(101);
			manager.bookRoom(102);
		} catch (InvalidChangeRoomStatusException e) {
			e.printStackTrace();
		}
		assertThrows(InvalidChangeRoomStatusException.class, () -> manager.checkAndBookFirstFreeRoom());
	}

	/**
	 * Test passes if all rooms are vacant
	 * after the manager clears the bookings
	 */
	@Test
	void clearBookingsShouldWorkEveryTime() {
		manager.clearBookings();
		for (Room room : manager.getHotel().getRooms()) {
			assertFalse(room.getRoomStatus());
		}
	}
}
