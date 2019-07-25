package eu.deltasource.internship.hotelcalifornia.services;

import eu.deltasource.internship.hotelcalifornia.commodities.AbstractCommodity;
import eu.deltasource.internship.hotelcalifornia.commodities.Bed;
import eu.deltasource.internship.hotelcalifornia.commodities.BedType;
import eu.deltasource.internship.hotelcalifornia.commodities.Shower;
import eu.deltasource.internship.hotelcalifornia.models.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class HotelServiceTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
		HotelService.getTakenRoomNumbers().clear();
		HotelService.getCommodityRoomMap().clear();
		HotelService.getAvailableCommoditiesSet().clear();
	}

	@Test
	void getRoomNumberIfValidShouldReturnNumberIfUnique() {
		assertEquals(101, HotelService.getRoomNumberIfValid(101));
	}

	@Test
	void getRoomNumberIfValidShouldReturnZeroIfNumberNotUnique() {
		//Given
		HotelService.getRoomNumberIfValid(101);

		//Then
		assertEquals(0, HotelService.getRoomNumberIfValid(101));
	}

	@Test
	void makeCommodityAvailable() {
		//Given
		Bed bed = new Bed(BedType.SINGLE);
		HotelService.getCommodityRoomMap().put(bed, null);

		//When
		HotelService.makeCommodityAvailable(bed);

		//Then
		assertFalse(HotelService.getCommodityRoomMap().containsKey(bed));
		assertTrue(HotelService.getAvailableCommoditiesSet().contains(bed));
	}

	@Test
	void getCommodityLocationShouldReturnRoomIfCommodityIsSomewhere() {
		//Given
		Bed bed = new Bed(BedType.SINGLE);
		Room room = new Room(101, new HashSet<>(Arrays.asList(bed)));

		//Then
		assertEquals(room, HotelService.getCommodityLocation(bed));
	}

	@Test
	void getCommodityLocationShouldReturnNullIfCommodityIsVacant() {
		//Given
		Bed bed = new Bed(BedType.DOUBLE);

		//Then
		assertNull(HotelService.getCommodityLocation(bed));
	}

	@Test
	void addCommodityToRoomShouldReplaceItIfInAnotherRoom() {
		//Given
		Bed bed = new Bed(BedType.DOUBLE);
		Room room1 = new Room(101,new HashSet<>(Arrays.asList(bed)));
		Room room2 = new Room(102, new HashSet<>());

		//When
		HotelService.addCommodityToRoom(bed, room2);

		//Then
		assertEquals(room2, HotelService.getCommodityRoomMap().get(bed));
		assertFalse(HotelService.getAvailableCommoditiesSet().contains(bed));

	}

	@Test
	void prepareCommoditiesForRoom() {
		//Given
		Bed bed = new Bed(BedType.DOUBLE);
		Set<AbstractCommodity> commoditySet = new HashSet<>(Arrays.asList(bed, new Bed(BedType.KING_SIZE), new Shower()));
		Room room = new Room(101,new HashSet<>(Arrays.asList(bed)));
		Room room1 = new Room(102, commoditySet);

		//When
		HotelService.prepareCommoditiesForRoom(commoditySet, room);

		//Then
		assertThat("The new commodity set is still in hashmap!",HotelService.getCommodityRoomMap().keySet(),
			not(containsInAnyOrder(commoditySet)));
		assertFalse(room.getCommodities().contains(bed));
		assertFalse(HotelService.getCommodityRoomMap().containsKey(bed));
	}

	@Test
	void checkIfDatesChronologicalShouldReturnFalseIfAnyDateIsNull() {
		assertFalse(HotelService.checkIfDatesChronological(null, LocalDate.of(2020, 1, 1)));
	}

	@Test
	void checkIfDatesChronologicalShouldReturnFalseIfNot() {
		assertFalse(HotelService.checkIfDatesChronological(LocalDate.of(2019, 9, 18),
			LocalDate.of(2019, 9, 12)));
	}

	@Test
	void checkIfDatesChronologicalShouldReturnTrueIfTheyAre() {
		assertTrue(HotelService.checkIfDatesChronological(LocalDate.of(2019, 9, 12),
			LocalDate.of(2019, 9, 18)));
	}
}
