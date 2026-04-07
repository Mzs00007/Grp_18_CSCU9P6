package vcfs.models.structure;

import java.util.*;
import vcfs.core.CareerFair;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * A participating company/organization in the fair.
 */
public class Organization {

	private Collection<Booth> booths;
	private CareerFair fair;
	private String name;

	/**
	 * Create an Organization with a name.
	 * @param name The organization name (cannot be empty)
	 * @throws IllegalArgumentException if name is invalid
	 */
	public Organization(String name) {
		setName(name);
		this.booths = new ArrayList<>();
		this.fair = null;
	}

	/**
	 * Get the organization name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the organization name.
	 * @param name The name (cannot be empty)
	 * @throws IllegalArgumentException if name is invalid
	 */
	public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Organization name cannot be empty");
		}
		this.name = name;
	}

	/**
	 * Get all booths in this organization.
	 */
	public Collection<Booth> getBooths() {
		return Collections.unmodifiableCollection(booths);
	}

	/**
	 * Get the career fair this organization belongs to.
	 */
	public CareerFair getFair() {
		return fair;
	}

	/**
	 * Set the career fair this organization belongs to.
	 * @param fair The career fair
	 */
	public void setFair(CareerFair fair) {
		this.fair = fair;
	}

	/**
	 * Add a booth to this organization.
	 * @param booth The booth to add (cannot be null)
	 * @throws IllegalArgumentException if booth is null
	 */
	public void addBooth(Booth booth) {
		if (booth == null) {
			throw new IllegalArgumentException("Booth cannot be null");
		}
		booths.add(booth);
		booth.setOrganization(this);
		Logger.log(LogLevel.INFO, "Booth added to organization: " + name);
	}

	/**
	 * Remove a booth from this organization.
	 * @param booth The booth to remove
	 */
	public void removeBooth(Booth booth) {
		if (booth != null && booths.remove(booth)) {
			Logger.log(LogLevel.INFO, "Booth removed from organization: " + name);
		}
	}

	@Override
	public String toString() {
		return "Organization{" +
				"name='" + name + '\'' +
				", booths=" + booths.size() +
				'}';
	}

}

