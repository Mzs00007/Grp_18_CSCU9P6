package vcfs.models.structure;

import java.util.*;
import vcfs.models.users.Recruiter;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * A company's presence at the fair; owns recruiters and a single VirtualRoom.
 */
public class Booth {

	private Collection<Recruiter> recruiters;
	private VirtualRoom room;
	private Organization organization;
	private String title;

	/**
	 * Create a Booth with a title.
	 * @param title The booth title (cannot be empty)
	 * @throws IllegalArgumentException if title is invalid
	 */
	public Booth(String title) {
		setTitle(title);
		this.recruiters = new ArrayList<>();
		this.room = null;
		this.organization = null;
	}

	/**
	 * Get all recruiters assigned to this booth.
	 */
	public Collection<Recruiter> getRecruiters() {
		return Collections.unmodifiableCollection(recruiters);
	}

	/**
	 * Get the booth's virtual room.
	 */
	public VirtualRoom getRoom() {
		return this.room;
	}

	/**
	 * Set the booth's virtual room.
	 * @param room The virtual room
	 */
	public void setRoom(VirtualRoom room) {
		this.room = room;
	}

	/**
	 * Get the organization this booth belongs to.
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * Set the organization this booth belongs to.
	 * @param organization The organization
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * Get the booth title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the booth title.
	 * @param title The title (cannot be empty)
	 * @throws IllegalArgumentException if title is invalid
	 */
	public void setTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Booth title cannot be empty");
		}
		this.title = title;
	}

	/**
	 * Assign a recruiter to this booth.
	 * @param recruiter The recruiter to assign (cannot be null)
	 * @throws IllegalArgumentException if recruiter is null
	 */
	public void assignRecruiter(Recruiter recruiter) {
		if (recruiter == null) {
			throw new IllegalArgumentException("Cannot assign null recruiter.");
		}
		recruiters.add(recruiter);
		recruiter.setBooth(this);
		Logger.log(LogLevel.INFO, "Recruiter assigned to booth: " + title);
	}

	/**
	 * Unassign a recruiter from this booth.
	 * @param recruiter The recruiter to remove
	 */
	public void removeRecruiter(Recruiter recruiter) {
		if (recruiter != null && recruiters.remove(recruiter)) {
			Logger.log(LogLevel.INFO, "Recruiter removed from booth: " + title);
		}
	}

	@Override
	public String toString() {
		return "Booth{" +
				"title='" + title + '\'' +
				", recruiters=" + recruiters.size() +
				", organization=" + (organization != null ? organization.getName() : "none") +
				'}';
	}

}

