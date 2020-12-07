package fr.orsys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.entities.Formateur;

public interface FormateurRepository extends JpaRepository<Formateur, Long>{

	boolean existsByNom(String nom);

	boolean existsByPrenom(String prenom);

}
