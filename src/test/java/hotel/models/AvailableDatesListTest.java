package hotel.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AvailableDatesListTest {

	@Test
	void setAvailableDatesShouldCreateEmptyListIfNullPassed() {
		AvailableDatesList availableDatesList = new AvailableDatesList(null);
		assertNotEquals(null,availableDatesList.getAvailableDates());
	}

	@Test
	void setAvailableDatesShouldAssignThePassedListIfNotNull() {
		AvailableDatesList availableDatesList = new AvailableDatesList(new ArrayList<>(Arrays.asList(
			LocalDate.of(2019,9,9),LocalDate.of(2019,9,10))));
		assertTrue(availableDatesList.getAvailableDates().size() == 2);
	}
}
