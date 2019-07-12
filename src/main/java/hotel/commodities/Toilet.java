package hotel.commodities;

public class Toilet extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("The toilet is being cleaned...");
	}
}
