package fr.orsys.metier;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import fr.orsys.entities.AppRole;
import fr.orsys.entities.AppUser;
import fr.orsys.entities.Centre;
import fr.orsys.entities.Formateur;
import fr.orsys.entities.Formation;

import fr.orsys.entities.Participant;


public interface OrsysMetier {
	public AppUser saveUser(AppUser user);
	public AppUser saveOldeUser(AppUser user);
	public AppRole saveRole(AppRole role);
	public void addRoleToUser(String username,String roleName);
	public AppUser findUserByUsername(String username);
	public Formation findByCode(String code);
	public Page<Formation> getAllFormationsPageable(int page, int size);
	public List<Centre> getAllCentres();
	public List<Formateur> getAllFormateurs();
	public void saveFormation(@Valid Formation formation);
	public boolean existsFormation(String code);
	public Optional<Formation>  getFormation(Long id);
	public Page<Participant> getAllParticipantsByFormation(Formation fo, int page, int size);
	public void deleteFormation(Formation formation);
	public Page<Centre> getAllCentresPageable(int page, int size);
	public void saveCentre(@Valid Centre formCentre);
	public boolean existsCentreByNom(String nom);
	public Centre getCentreById(Long id);
	public void deleteCentre(Centre centre);
	public Page<Formateur> getAllFormateursPageable(int page, int size);
	public void saveFromateur(@Valid Formateur formateur);
	public boolean existsFormateurByNom(String nom);
	public boolean existsFormateurByPrenom(String prenom);
	public Formateur getFormateurById(Long id);
	public void saveFormateur(@Valid Formateur formFormateur);
	public void deleteFormateur(Formateur formateur);
	public Page<Participant> getAllParticipantsPageable(int page, int size);
	public boolean existsParticipantByNom(String nom);
	public boolean existsParticipantByPrenom(String prenom);
	public Participant saveParticipant(@Valid Participant participant);
	public List<Formation> getAllFormation();
	public List<Participant> getAllParticipants();
	public Participant getParticipantById(Long id);
	public void deleteParticipant(Participant participant);
	public Page<AppUser> getAllUsersPageable(int page, int size);
	public boolean existsAppUserByNom(String username);
	public AppUser getUserById(Long id);
	public boolean existsAppUserById(Long id);
	public Page<AppUser> findByUsernameStartingWith(String username, int page, int size);
	public void deleteUser(AppUser user);
	public Page<Centre> findByNomStartingWith(String nom, int page, int size);
	public boolean existsCentreById(Long id);
}
