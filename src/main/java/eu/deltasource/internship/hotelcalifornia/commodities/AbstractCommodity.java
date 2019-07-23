package eu.deltasource.internship.hotelcalifornia.commodities;

import eu.deltasource.internship.hotelcalifornia.models.Hotel;

/**
 * Base class for concrete commodities
 * Declares one abstract method which is to be
 * overriden by subclasses
 *
 * @author Ivan Velkushanov
 */
public abstract class AbstractCommodity {
	public static int lastNumberUsed;
	public final int inventoryNumber;

	public AbstractCommodity() {
		inventoryNumber = ++lastNumberUsed;
	}

	public abstract void prepare();

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	/**
	 * Overriden hashCode method of AbstractCommodity type
	 *
	 * @return unique inventoryNumber
	 */
	@Override
	public int hashCode() {
		return inventoryNumber;
	}

	/**
	 * Overriden equals method - two commodities
	 * are equal if their inventory numbers are equal
	 *
	 * @param object the object to be compared
	 * @return false if it the argument is null or not of type AbstractCommodity
	 * false if it is not equal
	 * true if it is equal
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AbstractCommodity)) {
			return false;
		}
		AbstractCommodity commodity = (AbstractCommodity) object;
		return this.inventoryNumber == commodity.getInventoryNumber();
	}
}
