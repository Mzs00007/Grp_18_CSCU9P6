package vcfs.models.structure;

import java.util.*;
import vcfs.core.CareerFair;

/**
 * A participating company/organization in the fair.
 */
public class Organization {

	public Collection<Booth> booths;
	public CareerFair fair;
	public String name;

	/**
	 * Add a booth to this organization.
	 * @param booth
	 */
	public void addBooth(Booth booth) {
		// TODO - implement Organization.addBooth
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove a booth from this organization.
	 * @param booth
	 */
	public void removeBooth(Booth booth) {
		// TODO - implement Organization.removeBooth
		throw new UnsupportedOperationException();
	}

}

