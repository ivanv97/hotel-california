package hotel.commodities;

public class Shower extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("The shower is being cleaned...");
	}
}
