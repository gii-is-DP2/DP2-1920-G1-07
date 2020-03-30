
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "donation")
public class Donation extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User	user;
  
	@Column(name = "money")
	@DecimalMin("0.01")
	@NotNull
	private Double	money;

	@Column(name = "anonymous")
	private Boolean	anonymous;

	@ManyToOne
	private Cause	causes;
	
	@Column(name = "moneyRest") 
	private Double moneyRest;

}
