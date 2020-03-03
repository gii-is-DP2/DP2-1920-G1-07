package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity{
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "money")
	@NotEmpty
	private Double money;
	
	@Column(name = "anonymous")
	@NotEmpty
	private Boolean anonymous;
	
	//@ManyToOne
	//private Causes causes;
	
	
	
}