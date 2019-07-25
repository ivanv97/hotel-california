package eu.deltasource.internship.hotelcalifornia.services;

/**
 * Service class used for getting
 * unique commodity number
 */
public class CommodityService {
	private static int lastNumberUsed;

	public static int getCommodityNumber() {
		return ++lastNumberUsed;
	}
}
