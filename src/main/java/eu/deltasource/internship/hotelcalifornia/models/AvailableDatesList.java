package eu.deltasource.internship.hotelcalifornia.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for the availableDates of available dates
 *
 * @author Ivan Velkushanov
 */
public class AvailableDatesList {
	private List<LocalDate> availableDates;

	public AvailableDatesList(List<LocalDate> availableDates) {
		setAvailableDates(availableDates);
	}

	public List<LocalDate> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<LocalDate> availableDates) {
		this.availableDates = availableDates == null ? new ArrayList<>() : availableDates;
	}
}
