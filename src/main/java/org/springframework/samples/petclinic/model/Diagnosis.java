
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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

}
