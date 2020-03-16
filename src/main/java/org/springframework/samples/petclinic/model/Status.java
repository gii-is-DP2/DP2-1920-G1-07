
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status extends NamedEntity {
}
  