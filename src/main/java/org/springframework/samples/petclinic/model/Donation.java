package org.springframework.samples.petclinic.model;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

	//@ManyToOne
	//private Causes causes;
	
	
	
}