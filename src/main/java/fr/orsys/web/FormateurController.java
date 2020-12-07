package fr.orsys.web;



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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import fr.orsys.entities.Formateur;
import fr.orsys.metier.OrsysMetier;

@Controller
public class FormateurController {

	@Autowired
	private OrsysMetier orsysMetier;

	@RequestMapping(value = "/user/formateurs")
	public String formateurs(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "2") int size,
			@RequestParam(name = "errorMessage", defaultValue = "") String errorMessage) {

		Page<Formateur> listFromateurs = orsysMetier.getAllFormateursPageable(page, size);
		model.addAttribute("activePage", page);
		model.addAttribute("size", size);
		int[] taillePagination = IntStream.range(0, listFromateurs.getTotalPages()).toArray();
		model.addAttribute("taillePagination", taillePagination);
		model.addAttribute("listeFormateur", listFromateurs);
		model.addAttribute("errorMessage", errorMessage);
		return "formateurs";
	}

	@RequestMapping(value = "/admin/ajouterformateur")
	public String ajouterformateur(Model model) {
		model.addAttribute("formFormateur", new Formateur());
		return "ajouterformateur";
	}
	@PostMapping("/admin/ajouterformateur")
	public String ajouterformateur(@Valid @ModelAttribute(value = "formFormateur") Formateur formFormateur, BindingResult result,
			Model model) {
		if (orsysMetier.existsFormateurByNom(formFormateur.getNom()) && orsysMetier.existsFormateurByPrenom(formFormateur.getPrenom())) {
			result.rejectValue("nom", "error.formFormateur", "Le nom existe déja.");
			result.rejectValue("prenom", "error.formFormateur", "Le prénom existe déja.");
		}
		if (result.hasErrors()) {
			return "ajouterformateur";
		} else {
			orsysMetier.saveFromateur(formFormateur);
			return "redirect:/user/formateurs";
		}
	}
	@RequestMapping(value = "/admin/modifierformateur", method = RequestMethod.GET)
	public String modifierformateur(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
		Formateur formFormateur = orsysMetier.getFormateurById(id);
		model.addAttribute("formFormateur", formFormateur);
		return "modifierformateur";

	}

	@PostMapping("/admin/modifierformateur")
	public String modifierformateur(@Valid @ModelAttribute(value = "formFormateur") Formateur formFormateur, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "modifierformateur";
		} else {
			orsysMetier.saveFormateur(formFormateur);
			return "redirect:/user/formateurs";
		}
	}

	@RequestMapping(value = "/admin/supprimerformateur")
	public String supprimerformateur(@RequestParam(name = "id", defaultValue = "0") Long id) {
		Formateur formateur = orsysMetier.getFormateurById(id);
		try {
			orsysMetier.deleteFormateur(formateur);
			return "redirect:/user/formateurs";
		} catch (Exception e) {
			String msg = "Vous ne pouvez pas supprimer ce formateur.";
			return "redirect:/user/formateurs?errorMessage=" + msg;
		}
	}
}
