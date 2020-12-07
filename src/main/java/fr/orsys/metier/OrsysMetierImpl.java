package fr.orsys.metier;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.orsys.dao.CentreRepository;
import fr.orsys.dao.FormateurRepository;
import fr.orsys.dao.FormationRepository;
import fr.orsys.dao.ParticipantRepository;
import fr.orsys.dao.RoleRepository;
import fr.orsys.dao.UserRepository;
import fr.orsys.entities.AppRole;
import fr.orsys.entities.AppUser;
import fr.orsys.entities.Centre;
import fr.orsys.entities.Formateur;
import fr.orsys.entities.Formation;
import fr.orsys.entities.Participant;


@Service
public class OrsysMetierImpl implements OrsysMetier {
	@Autowired
	private FormationRepository formationRepository;
	@Autowired
	private ParticipantRepository participantRepository;
	@Autowired
	private CentreRepository centreRepository;
	@Autowired
	private FormateurRepository formateurRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Formation findByCode(String code) {
		return formationRepository.findByCode(code);
	}

	@Override
	public Page<Formation> getAllFormationsPageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return formationRepository.findAll(pageable);
	}

	@Override
	public List<Centre> getAllCentres() {
		return centreRepository.findAll();
	}

	@Override
	public List<Formateur> getAllFormateurs() {
		return formateurRepository.findAll();
	}

	@Override
	public void saveFormation(@Valid Formation formation) {
		formationRepository.save(formation);
	}

	@Override
	public boolean existsFormation(String code) {
		return formationRepository.existsByCode(code);
	}

	@Override
	public Optional<Formation> getFormation(Long id) {
		return formationRepository.findById(id);
	}

	@Override
	public Page<Participant> getAllParticipantsByFormation(Formation fo, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return participantRepository.findAllParticipantByFormation(fo, pageable);
	}

	@Override
	public void deleteFormation(Formation formation) {
		formationRepository.delete(formation);
	}

	@Override
	public Page<Centre> getAllCentresPageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return centreRepository.findAll(pageable);
	}

	@Override
	public void saveCentre(@Valid Centre formCentre) {
		centreRepository.save(formCentre);
	}

	@Override
	public boolean existsCentreByNom(String nom) {
		return centreRepository.existsByNom(nom);
	}

	@Override
	public Centre getCentreById(Long id) {
		return centreRepository.getOne(id);
	}

	@Override
	public void deleteCentre(Centre centre) {
		centreRepository.delete(centre);
	}

	@Override
	public Page<Formateur> getAllFormateursPageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return formateurRepository.findAll(pageable);
	}

	@Override
	public void saveFromateur(@Valid Formateur formateur) {
		formateurRepository.save(formateur);
	}

	@Override
	public boolean existsFormateurByNom(String nom) {
		return formateurRepository.existsByNom(nom);
	}

	@Override
	public boolean existsFormateurByPrenom(String prenom) {
		return formateurRepository.existsByPrenom(prenom);
	}

	@Override
	public Formateur getFormateurById(Long id) {
		return formateurRepository.getOne(id);
	}

	@Override
	public void saveFormateur(@Valid Formateur formFormateur) {
		formateurRepository.save(formFormateur);

	}

	@Override
	public void deleteFormateur(Formateur formateur) {
		formateurRepository.delete(formateur);
	}

	@Override
	public Page<Participant> getAllParticipantsPageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return participantRepository.findAll(pageable);
	}

	@Override
	public boolean existsParticipantByNom(String nom) {
		return participantRepository.existsByNom(nom);
	}

	@Override
	public boolean existsParticipantByPrenom(String prenom) {
		return participantRepository.existsByPrenom(prenom);
	}

	@Override
	public Participant saveParticipant(@Valid Participant participant) {
		return participantRepository.save(participant);

	}

	@Override
	public List<Formation> getAllFormation() {
		return formationRepository.findAll();
	}

	@Override
	public Participant getParticipantById(Long id) {
		return participantRepository.getOne(id);
	}

	@Override
	public List<Participant> getAllParticipants() {
		return participantRepository.findAll();
	}

	@Override
	public void deleteParticipant(Participant participant) {
		participantRepository.delete(participant);
	}
	@Override
	public AppUser saveOldeUser(AppUser user) {
		return userRepository.save(user);
	}
	@Override
	public AppUser saveUser(AppUser user) {
		String hashPW = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(hashPW);
		return userRepository.save(user);
	}
	@Override
	public AppRole saveRole(AppRole role) {
		return roleRepository.save(role);
	}
	@Override
	public void addRoleToUser(String username, String roleName) {	
		AppRole role=roleRepository.findByRoleName(roleName);
		AppUser user = userRepository.findByUsername(username);
		user.getRoles().add(role);
		userRepository.save(user);
		
	}
	@Override
	public AppUser findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Page<AppUser> getAllUsersPageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return userRepository.findAll(pageable);
	}

	@Override
	public boolean existsAppUserByNom(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public AppUser getUserById(Long id) {
		return userRepository.getOne(id);
	}

	@Override
	public boolean existsAppUserById(Long id) {
		return userRepository.existsById(id);
	}

	@Override
	public Page<AppUser> findByUsernameStartingWith(String username, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return userRepository.findByUsernameStartingWith(username,pageable);
	}

	@Override
	public void deleteUser(AppUser user) {
	  userRepository.delete(user);
	}

	@Override
	public Page<Centre> findByNomStartingWith(String nom, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
		return centreRepository.findByNomStartingWith(nom,pageable);
	}

	@Override
	public boolean existsCentreById(Long id) {
		return centreRepository.existsById(id);
	}

	


}
