package eu.deltasource.internship.hotelcalifornia.commodities;

/**
 * Bed commodity - extends AbstractCommodity
 * Provides implementation for abstract method
 *
 * @author Ivan Velkushanov
 */
public class Bed extends AbstractCommodity {
	private BedType bedType;

	public Bed(BedType bedType) {
		this.bedType = bedType;
	}

	public BedType getBedType() {
		return bedType;
	}

	@Override
	public void prepare() {
		System.out.println("Bed sheets are being replaced...");
	}
}
