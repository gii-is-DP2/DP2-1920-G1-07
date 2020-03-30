package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "reservation")
public class Reservation extends BaseEntity{
	
	
	@Column(name = "entry_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate entryDate;
	
	@Column(name="exit_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate exitDate;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	private Status status;
	
	@NotEmpty
	@Column(name="pet")
	private String pet;
	
	@ManyToOne
	@JoinColumn(name="owner_id")
	private Owner owner;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;


}
