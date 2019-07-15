package hotel.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for the list of available dates
 * @author Ivan Velkushanov
 */
public class AvailableDatesList {
	private List<LocalDate> list = new ArrayList<>();

	public AvailableDatesList(List<LocalDate> list){
		this.list = list;
	}

	public List<LocalDate> getList() {
		return list;
	}

	public void setList(List<LocalDate> list) {
		this.list = list;
	}
}
