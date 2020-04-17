
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "sitters")
public class Sitter extends Person {

	@Column(name = "address")
	@NotEmpty
	private String	address;

	@Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String	telephone;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType	type;

	//
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User	user;
	//


	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(final PetType type) {
		this.type = type;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return this.getUser().getUsername();
	}

}
