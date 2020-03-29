
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "diagnosis")
public class Diagnosis extends BaseEntity {

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	date;

	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet			vet;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet			pet;

	@OneToOne()
	@JoinColumn(name = "visit_id")
	private Visit		visit;


	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public Vet getVet() {
		return this.vet;
	}

	public void setVet(final Vet vet) {
		this.vet = vet;
	}

	public Pet getPet() {
		return this.pet;
	}

	public void setPet(final Pet pet) {
		this.pet = pet;
	}

	public Visit getVisit() {
		return this.visit;
	}

	public void setVisit(final Visit visit) {
		this.visit = visit;
	}
	@Override
	public String toString() {
		
		return new ToStringCreator(this)
				.append("id",this.id)
				.append("capacity",this.description)
				.append("date",this.date).toString();
	}
}
