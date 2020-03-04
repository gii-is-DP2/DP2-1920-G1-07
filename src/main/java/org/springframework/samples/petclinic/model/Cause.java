
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.core.style.ToStringCreator;

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
	@NotEmpty
	private Double		money;

	@Column(name = "deadline")
	@NotEmpty
	private LocalDate	deadline;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User		user;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status		status;


	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(final Double money) {
		this.money = money;
	}

	public LocalDate getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final LocalDate deadline) {
		this.deadline = deadline;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("title", this.getTitle()).append("description", this.getDescription()).append("money", this.getMoney()).append("deadline", this.getDeadline()).toString();
	}

}
