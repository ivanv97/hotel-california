package hotel.commodities;

/**
 * Toilet commodity - extends AbstractCommodity
 * Provides implementation for abstract method
 * @author Ivan Velkushanov
 */
public class Toilet extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("The toilet is being cleaned...");
	}
}
