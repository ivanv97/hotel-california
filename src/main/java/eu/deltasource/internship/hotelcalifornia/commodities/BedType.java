package eu.deltasource.internship.hotelcalifornia.commodities;

/**
 * Bed Type is an enum
 * which is used in Bed commodity class -
 * it provides for determining the type and
 * respectively the capacity of the bed
 */
public enum BedType {
	SINGLE(1), DOUBLE(2), KING_SIZE(2);

	public int getSize() {
		return size;
	}

	private final int size;

	BedType(int size){
		this.size = size;
	}
}
