package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;


@Entity
@Table(name = "room")
public class Room extends NamedEntity {
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;
	
//	@ManyToOne
//	@JoinColumn(name="sitter_id")
//	private Sitter sitter;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Pet> pets;

//	@OneToOne
//	@JoinColumn(name="reservation_id")
//	private Reservation reservation;
	
	
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

//	public Sitter getSitter() {
//		return sitter;
//	}
//
//	public void setSitter(Sitter sitter) {
//		this.sitter = sitter;
//	}

	public Set<Pet> getPetsInternal() {
		if(this.pets == null) {
			this.pets = new HashSet<Pet>();
		}
		return this.pets;
	}

	public void setPetsInternal(Set<Pet> pets) {
		this.pets = pets;
	}
	
	public List<Pet> getPets() {
		List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name",true,true));
		return Collections.unmodifiableList(sortedPets);
	}
	
	public void addPet(Pet pet) {
		getPetsInternal().add(pet);
		pet.setRoom(this);
	}
	
	public Pet getPet(String name) {
		return getPet(name,false);
	}
	
	public Pet getPet(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for(Pet pet : getPetsInternal()) {
			if(!ignoreNew || !pet.isNew()) {
				String compName = pet.getName();
				compName = compName.toLowerCase();
				if(compName.equals(name)) {
					return pet;
				}
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id",this.id)
				.append("capacity",this.capacity).toString();
	}
	
	
}
