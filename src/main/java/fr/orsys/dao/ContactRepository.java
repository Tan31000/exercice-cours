package fr.orsys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

}
