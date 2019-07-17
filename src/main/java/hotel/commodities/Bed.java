package hotel.commodities;

/**
 * Bed commodity - extends AbstractCommodity
 * Provides implementation for abstract method
 * @author Ivan Velkushanov
 */
public class Bed extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("Bed sheets are being replaced...");
	}
}
