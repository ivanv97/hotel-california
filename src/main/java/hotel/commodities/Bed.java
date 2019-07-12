package hotel.commodities;

public class Bed extends AbstractCommodity {
	@Override
	public void prepare() {
		System.out.println("Bed sheets are being replaced...");
	}
}
