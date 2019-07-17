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

class HotelTest {
	private Set<AbstractCommodity> roomCommodities = new HashSet<>(Arrays.asList(new Bed(), new Shower(), new Toilet()));
	private Room room = new Room(101, roomCommodities);

	@Test
	void constructorShouldSetDefaultValuesWhenNullArguments() {
		//when
		Hotel hotel = new Hotel(null, null);
		//then
		assertEquals("Unknown", hotel.getName());
		assertTrue(hotel.getRooms().isEmpty());
	}

	@Test
	void constructorShouldAssignFieldsWhenValidArguments() {
		//when
		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room)));
		//then
		assertEquals("California", hotel.getName());
		assertFalse(hotel.getRooms().isEmpty());
	}

	@Test
	void getFreeRoomsAndDatesShouldThrowExcIfDayNumberIsNotValid() {
		//when
		Hotel hotel = new Hotel("California", new ArrayList<>(Arrays.asList(room)));
		//then
		assertThrows(InvalidHotelActionException.class, () -> hotel.getFreeRoomsAndDates(LocalDate.of(2019, 7, 18),
			LocalDate.of(2019, 7, 25), 5, 2));
	}

	@Test
	void getFreeRoomsAndDatesShouldWorkIfAvailableDates() {
		//given
		Hotel hotel = new Hotel("California", new ArrayList<>());
		hotel.getRooms().add(room);
		Map<Room, AvailableDatesList> roomsAndDates = null;
		//when
		try {
			room.createBooking(LocalDate.of(2019, 07, 20),
				LocalDate.of(2019, 07, 22), "Ivan", 1234567895L);
			roomsAndDates = hotel.getFreeRoomsAndDates(LocalDate.of(2019, 7, 19),
				LocalDate.of(2019, 7, 20), 1, 1);
		} catch (InvalidHotelActionException e) {
			e.printStackTrace();
		}
		//then
		assertDoesNotThrow(() -> hotel.getFreeRoomsAndDates(LocalDate.of(2019, 7, 19),
			LocalDate.of(2019, 7, 20), 1, 1));
		assertTrue(roomsAndDates.get(room).getList().contains(LocalDate.of(2019, 7, 19))
			&& roomsAndDates.get(room).getList().size() == 1);
	}
}
