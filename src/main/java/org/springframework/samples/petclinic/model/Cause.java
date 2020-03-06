
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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


	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("title", this.getTitle()).append("description", this.getDescription()).append("money", this.getMoney()).append("deadline", this.getDeadline()).toString();
	}

}
