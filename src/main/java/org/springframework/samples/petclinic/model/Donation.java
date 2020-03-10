  
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity{
	
	@ManyToOne    
	@JoinColumn(name = "user_id")
	private User user;
	 
	@Column(name = "money")
	private Double money;
	
	@Column(name = "anonymous")
	private Boolean anonymous;
	@Column(name = "nombre")
	private String nombre;

	@ManyToOne
	private Cause causes;
	
	
	
}