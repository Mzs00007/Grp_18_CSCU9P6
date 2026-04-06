package vcfs.models.structure;

import java.util.*;
import vcfs.models.users.Recruiter;

/**
 * A company's presence at the fair; owns recruiters and a single VirtualRoom.
 */
public class Booth {

	public Collection<Recruiter> recruiters;
	public VirtualRoom room;
	public Organization organization;
	public String title;

	/**
	 * Assign a recruiter to this booth.
	 * @param recruiter
	 */
	public void assignRecruiter(Recruiter recruiter) {
		if (recruiter == null) {
			throw new IllegalArgumentException("[Booth] Cannot assign null recruiter.");
		}
		if (this.recruiters == null) {
			this.recruiters = new ArrayList<>();
		}
		this.recruiters.add(recruiter);
	}

	/**
	 * Unassign a recruiter from this booth.
	 * @param recruiter
	 */
	public void removeRecruiter(Recruiter recruiter) {
		if (recruiter != null && this.recruiters != null) {
			this.recruiters.remove(recruiter);
		}
	}

	/**
	 * Return the booth's virtual room.
	 */
	public VirtualRoom getRoom() {
		return this.room;
	}

}

