package fr.orsys.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.entities.Centre;


public interface CentreRepository extends JpaRepository<Centre, Long>{

	public Page<Centre> findAll(Pageable pageable);

	public boolean existsByNom(String nom);

	public Page<Centre> findByNomStartingWith(String nom, Pageable pageable);
}
