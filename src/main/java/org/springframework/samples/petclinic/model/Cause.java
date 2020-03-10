
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "cause")
public class Cause extends BaseEntity {

	@Column(name = "title")
	@NotEmpty
	private String		title;

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@Column(name = "money")
	private Double		money;

	@Column(name = "deadline")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	deadline;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User		user;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status		status;
	
	@OneToMany
	private Set<Donation> donations;

	
	protected Set<Donation> getDonationsInternal() {
		if (this.donations == null) {
			this.donations = new HashSet<>();
		}
		return this.donations;
	}

	protected void setDonationsInternal(Set<Donation> donations) {
		this.donations = donations;
	}

	public List<Donation> getDonations() {
		List<Donation> sortedDonations = new ArrayList<>(getDonationsInternal());
		PropertyComparator.sort(sortedDonations, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedDonations);
	}

	public void addDonation(Donation donation) {
		getDonationsInternal().add(donation);
		donation.setCauses(this);
	}
	
	public boolean removeDonation(Donation donation) {
		return getDonationsInternal().remove(donation);
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("title", this.getTitle()).append("description", this.getDescription()).append("money", this.getMoney()).append("deadline", this.getDeadline()).toString();
	}

}
