package fr.orsys.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.entities.AppUser;


public interface UserRepository extends JpaRepository<AppUser, Long> {
	public AppUser findByUsername(String username);

	public boolean existsByUsername(String username);

	public Page<AppUser> findByUsernameStartingWith(String username, Pageable pageable);
}