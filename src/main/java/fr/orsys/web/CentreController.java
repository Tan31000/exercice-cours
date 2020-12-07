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

import org.springframework.web.bind.annotation.RequestParam;

import fr.orsys.entities.Centre;
import fr.orsys.metier.OrsysMetier;

@Controller
public class CentreController {
	@Autowired
	private OrsysMetier orsysMetier;

	@RequestMapping(value = "/user/centres")
	public String centres(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "2") int size,
			@RequestParam(name = "errorMessage", defaultValue = "") String errorMessage) {
		Page<Centre> listCentres = orsysMetier.getAllCentresPageable(page, size);
		model.addAttribute("activePage", page);
		model.addAttribute("size", size);
		int[] taillePagination = IntStream.range(0, listCentres.getTotalPages()).toArray();
		model.addAttribute("taillePagination", taillePagination);
		model.addAttribute("listeCentres", listCentres);
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("formCentre", new Centre());
		model.addAttribute("search", false);
		return "centres";
	}

	@RequestMapping("/user/recherchecentre")
	public String recherchecentre(@Valid @ModelAttribute(value = "formCentre") Centre formCentre, BindingResult result,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "2") int size, Model model) {
		if (result.hasFieldErrors("nom")) {
			return "centres";
		} else {
			Page<Centre> listCentres = orsysMetier.findByNomStartingWith(formCentre.getNom(), page, size);
			model.addAttribute("activePage", page);
			model.addAttribute("size", size);
			int[] taillePagination = IntStream.range(0, listCentres.getTotalPages()).toArray();
			model.addAttribute("taillePagination", taillePagination);
			model.addAttribute("listeCentres", listCentres);
			model.addAttribute("formCentre", formCentre);
			model.addAttribute("nom", formCentre.getNom());
			model.addAttribute("search", true);
			return "centres";
		}
	}


	@RequestMapping(value = "/manager/editcentre")
	public String editcentre(Model model, @RequestParam(name = "id", defaultValue = "0") Long id) {
		if (id > 0) {
			if (!orsysMetier.existsCentreById(id)) {
				String msg = "Le Centre n'existe pas!";
				return "redirect:/user/centres?errorMessage=" + msg;
			} else {
				Centre formCentre = orsysMetier.getCentreById(id);
				model.addAttribute("formCentre", formCentre);
				model.addAttribute("task", "Modifier");
				model.addAttribute("id", id);
			}
		} else {
			model.addAttribute("formCentre", new Centre());
			model.addAttribute("task", "Ajouter");
		}
		return "editcentre";
	}

	@PostMapping("/manager/editcentre")
	public String editcentre(Model model,@Valid @ModelAttribute(value = "formCentre") Centre formCentre, BindingResult result) {
		if (result.hasErrors()) {
			return "editcentre";
		}
		if (formCentre.getId() == null) {
			if (orsysMetier.existsCentreByNom(formCentre.getNom())) {
				result.rejectValue("nom", "error.formCentre", "Le nom existe déja.");
				model.addAttribute("task", "Ajouter");
				return "editcentre";
			}
		}
		orsysMetier.saveCentre(formCentre);
		return "redirect:/user/centres";
	}

	@RequestMapping(value = "/admin/supprimercentre")
	public String supprimercentre(@RequestParam(name = "id", defaultValue = "0") Long id) {
		Centre centre = orsysMetier.getCentreById(id);
		try {
			orsysMetier.deleteCentre(centre);
			return "redirect:/user/centres";
		} catch (Exception e) {
			String msg = "Vous ne pouvez pas supprimer ce centre?";
			return "redirect:/user/centres?errorMessage=" + msg;
		}
	}
}
