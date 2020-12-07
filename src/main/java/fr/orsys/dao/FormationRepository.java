package fr.orsys.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import fr.orsys.entities.Formation;

public interface FormationRepository extends JpaRepository<Formation, Long> {
	public Formation findByCode(String code);

	public Page<Formation> findAll(Pageable pageable);

	public boolean existsByCode(String code);

}
