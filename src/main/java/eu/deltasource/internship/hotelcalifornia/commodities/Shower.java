package eu.deltasource.internship.hotelcalifornia.commodities;

/**
 * Shower commodity - extends AbstractCommodity
 * Provides implementation for abstract method
 * @author Ivan Velkushanov
 */
public class Shower extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("The shower is being cleaned...");
	}
}
