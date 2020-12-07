package fr.orsys.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Centre implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private Long id;
	private String nom;
	private String description;
	@OneToOne
	private Contact contact;
	/* **********UNIDERECT*************
	 @OneToMany
	private Collection<Formation> formation;
	*/
	@OneToMany(mappedBy = "centre", fetch=FetchType.LAZY)
	private Collection<Formation> formation;
}