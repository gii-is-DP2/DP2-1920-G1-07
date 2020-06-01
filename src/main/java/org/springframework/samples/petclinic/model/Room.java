
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "room")
public class Room extends NamedEntity {

	@Column(name = "capacity")
	private Integer				capacity;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType				type;

	@ManyToOne
	@JoinColumn(name = "sitter_id")
	private Sitter				sitter;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Pet>			pets;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Reservation>	reservations;


	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final Integer capacity) {
		this.capacity = capacity;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(final PetType type) {
		this.type = type;
	}

	public Set<Reservation> getReservations() {
		return this.reservations;
	}

	public Sitter getSitter() {
		return this.sitter;
	}

	public void setSitter(final Sitter sitter) {
		this.sitter = sitter;
	}

	public void setReservations(final Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<Pet> getPetsInternal() {
		if (this.pets == null) {
			this.pets = new HashSet<>();
		}
		return this.pets;
	}

	public void setPetsInternal(final Set<Pet> pets) {
		this.pets = pets;
	}

	public List<Pet> getPets() {
		List<Pet> sortedPets = new ArrayList<>(this.getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedPets);
	}

	public void addPet(final Pet pet) {
		this.getPetsInternal().add(pet);
		pet.setRoom(this);
	}

	public Pet getPet(final String name) {
		return this.getPet(name, false);
	}

	public Pet getPet(String name, final boolean ignoreNew) {
		name = name.toLowerCase();
		for (Pet pet : this.getPetsInternal()) {
			if (!ignoreNew || !pet.isNew()) {
				String compName = pet.getName();
				compName = compName.toLowerCase();
				if (compName.equals(name)) {
					return pet;
				}
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.id).append("capacity", this.capacity).append("type", this.type).toString();
	}

}
