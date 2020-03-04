package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name="sitter")
public class Sitter extends Person{
	
	@Column(name="address")
	@NotEmpty
	private String address;
	
	@Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;
	
	@ManyToOne
	@JoinColumn(name="type_id")
	private PetType type;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sitter")
	private Set<Pet> pets;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username",referencedColumnName = "username")
	private User user;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public PetType getType() {
		return type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Set<Pet> getPetsInternal() {
		return pets;
	}

	public void setPetsInternal(Set<Pet> pets) {
		this.pets = pets;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	} 

	public List<Pet> getPets() {
		List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name",true,true));
		return Collections.unmodifiableList(sortedPets);
	}
	
	public void addPet(Pet pet) {
		getPetsInternal().add(pet);
		pet.setSitter(this);
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
	
	
}
