package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;


@Entity
@Table(name = "room")
public class Room extends NamedEntity {
	
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;
	
//	@ManyToOne
//	@JoinColumn(name="sitter_id")
//	private Sitter sitter;
	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Reservation> reservations;
	
	
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public PetType getType() {
		return type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id",this.id)
				.append("capacity",this.capacity)
				.append("type",this.type).toString();
	}
	
	
}
