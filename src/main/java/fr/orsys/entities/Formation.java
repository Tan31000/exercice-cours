package fr.orsys.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor 
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("MERE")
public class Formation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id	@GeneratedValue
	private Long id;
	@NotNull(message = "Vous devez remplir le champ code!")
	@NotEmpty(message = "Le code ne doit pas être vide.") 
	@NotBlank(message = "Le code ne doit pas contenir d'espace!") 
	@Size(min = 3, max=5)
	private String code;
	private String theme;
	private double prix;
	@ManyToOne
	private Formateur formateur;
	@ManyToOne
    private Centre centre;
	@ManyToMany(mappedBy = "formation", fetch = FetchType.LAZY)
	Collection<Participant> participant;
}
