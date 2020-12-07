package fr.orsys.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.orsys.entities.AppRole;
import fr.orsys.entities.AppUser;
import fr.orsys.metier.OrsysMetier;

@Controller
public class UserController {
	@Autowired
	private OrsysMetier orsysMetier;

	@RequestMapping(value = "/admin/users")
	public String users(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "2") int size,
			@RequestParam(name = "errorMessage", defaultValue = "") String errorMessage) {

		Page<AppUser> listUsers = orsysMetier.getAllUsersPageable(page, size);
		model.addAttribute("activePage", page);
		model.addAttribute("size", size);
		int[] taillePagination = IntStream.range(0, listUsers.getTotalPages()).toArray();
		model.addAttribute("taillePagination", taillePagination);
		model.addAttribute("listeUsers", listUsers);
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("formUser", new AppUser());
		model.addAttribute("search", false);
		return "users";
	}

	@RequestMapping("/admin/rechercheuser")
	public String rechercheuser(@Valid @ModelAttribute(value = "formUser") AppUser formUser, BindingResult result,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "2") int size, Model model) {
		if (result.hasFieldErrors("username")) {
			return "uesrs";
		} else {
			Page<AppUser> listUsers = orsysMetier.findByUsernameStartingWith(formUser.getUsername(), page, size);
			model.addAttribute("activePage", page);
			model.addAttribute("size", size);
			int[] taillePagination = IntStream.range(0, listUsers.getTotalPages()).toArray();
			model.addAttribute("taillePagination", taillePagination);
			model.addAttribute("listeUsers", listUsers);
			model.addAttribute("formUser", formUser);
			model.addAttribute("username", formUser.getUsername());
			model.addAttribute("search", true);
			return "users";
		}
	}

	@RequestMapping(value = "/admin/activeuser")
	public String activeuser(Model model, @RequestParam(name = "id", defaultValue = "0") Long id,
			@RequestParam(name = "active", defaultValue = "0") Integer active) {
		if (id > 0) {
			if (!orsysMetier.existsAppUserById(id)) {
				String msg = "L'utilisateur n'existe pas!";
				return "redirect:/admin/users?errorMessage=" + msg;

			} else {
				AppUser currentUser = orsysMetier.getUserById(id);
				currentUser.setActive(active);
				orsysMetier.saveOldeUser(currentUser);
			}
		}
		return "redirect:/admin/users";
	}

	@RequestMapping(value = "/admin/edituser")
	public String edituser(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
		if (id > 0) {
			if (!orsysMetier.existsAppUserById(id)) {
				String msg = "L'utilisateur n'existe pas!";
				return "redirect:/admin/users?errorMessage=" + msg;
			} else {
				AppUser formUser = orsysMetier.getUserById(id);
				model.addAttribute("formUser", formUser);
				model.addAttribute("task", "Modifier");
				model.addAttribute("id", id);
				model.addAttribute("task", "Modifier");
				List<String> roles = formUser.getRoles().stream().map(AppRole::getRoleName)
						.collect(Collectors.toList());
				if (roles.contains("ADMIN")) {
					model.addAttribute("role", "ADMIN");
				} else if (roles.contains("MANAGER")) {
					model.addAttribute("role", "MANAGER");
				} else {
					model.addAttribute("role", "USER");
				}
			}
		} else {
			model.addAttribute("formUser", new AppUser());
			model.addAttribute("task", "Ajouter");
			model.addAttribute("role", "USER");
		}
		return "edituser";
	}

	@PostMapping("/admin/edituser")
	public String edituser(@Valid @ModelAttribute(value = "formUser") AppUser formUser, BindingResult result,
			Model model, @RequestParam(name = "role", defaultValue = "") String role) {
		if (result.hasErrors()) {
			return "edituser";
		}
		AppUser newUser = new AppUser();
		if (formUser.getId() != null && orsysMetier.existsAppUserById(formUser.getId())) {
			AppUser oldUser = orsysMetier.getUserById(formUser.getId());
			newUser.setId(oldUser.getId());
			newUser.setUsername(oldUser.getUsername());
			newUser.setActive(formUser.getActive());
			if (formUser.getPassword().isEmpty()) {
				newUser.setPassword(oldUser.getPassword());
				orsysMetier.saveOldeUser(newUser);
			} else {
				newUser.setPassword(formUser.getPassword());
				orsysMetier.saveUser(newUser);
			}

		} else {
			if (orsysMetier.existsAppUserByNom(formUser.getUsername())) {
				result.rejectValue("username", "error.formUser", "Le nom existe déja.");
				model.addAttribute("task", "Ajouter");
				model.addAttribute("role", "USER");
				return "edituser";
			}
			newUser.setUsername(formUser.getUsername());
			newUser.setPassword(formUser.getPassword());
			newUser.setActive(formUser.getActive());
			orsysMetier.saveUser(newUser);
		}

		if (role.equals("USER")) {
			orsysMetier.addRoleToUser(newUser.getUsername(), "USER");
		} else if (role.equals("MANAGER")) {
			orsysMetier.addRoleToUser(newUser.getUsername(), "USER");
			orsysMetier.addRoleToUser(newUser.getUsername(), "MANAGER");
		} else if (role.equals("ADMIN")) {
			orsysMetier.addRoleToUser(newUser.getUsername(), "USER");
			orsysMetier.addRoleToUser(newUser.getUsername(), "MANAGER");
			orsysMetier.addRoleToUser(newUser.getUsername(), "ADMIN");
		}
		return "redirect:/admin/users";
	}

	@RequestMapping(value = "/admin/supprimeruser")
	public String supprimercentre(@RequestParam(name = "id", defaultValue = "0") Long id) {
		AppUser user = orsysMetier.getUserById(id);
		try {
			orsysMetier.deleteUser(user);
			return "redirect:/admin/users";
		} catch (Exception e) {
			String msg = "Vous ne pouvez pas supprimer cet utilisateur.";
			return "redirect:/admin/users?errorMessage=" + msg;
		}
	}

}
