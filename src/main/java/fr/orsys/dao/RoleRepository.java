package fr.orsys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.entities.AppRole;



public interface RoleRepository extends JpaRepository<AppRole, Long> {
	public AppRole findByRoleName(String roleName);
}
