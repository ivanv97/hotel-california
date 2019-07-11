package hotel.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
	private Room room1;
	private Room room2;

	@BeforeEach
	void setUp() {
		room1 = new Room(101);
	}

	@AfterEach
	void tearDown() {
		Hotel.getTakenRoomNumbers().clear();
	}

	/**
	 * Tests if we can instantiate another room
	 * with a different number - test passes if
	 * the newly assigned number is the same as intended
	 */
	@Test
	void setNumberShouldWorkWhenNumberIsUnique() {
		room2 = new Room(102);
		assertEquals(102, room2.getNumber());
	}

	/**
	 * Test passes if we are not allowed to
	 * create a room object with the same number
	 * as the first one
	 */
	@Test
	void setNumberShouldNotWorkWhenNumberIsNotUnique() {
		room2 = new Room(room1.getNumber());
		assertNotEquals(room1.getNumber(), room2.getNumber());
	}

	/**
	 * Test passes if we manage to book a vacant room
	 */
	@Test
	void changeRoomStatusToBookedShouldReturnTrueIfVacant() {
		assertTrue(room1.changeRoomStatus(true));
	}

	/**
	 * Test passes if we are not allowed to clear booking
	 * of an already booked room
	 */
	@Test
	void changeRoomStatusToBookedShouldReturnFalseIfBooked() {
		room1.changeRoomStatus(true);
		assertFalse(room1.changeRoomStatus(true));
	}

	/**
	 * Test passes if a room is to be made vacant and
	 * it is already booked
	 */
	@Test
	void changeRoomStatusToFreeShouldReturnTrueIfBooked() {
		room1.changeRoomStatus(true);
		assertTrue(room1.changeRoomStatus(false));
	}

	/**
	 * Test passes if we are not allowed to free a room
	 * that is already vacant
	 */
	@Test
	void changeRoomStatusToFreeShouldReturnFalseIfVacant() {
		assertFalse(room1.changeRoomStatus(false));
	}
}
