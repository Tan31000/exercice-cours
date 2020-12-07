package fr.orsys.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import fr.orsys.dao.FormationRepository;
import fr.orsys.entities.Formation;
@Component
public class FormationFormatter implements Formatter<Formation> {
	@Autowired
	private FormationRepository formationRepository;

	@Override
	public String print(Formation formation, Locale locale) {
		return String.valueOf(formation.getId());
	}

	@Override
	public Formation parse(String id, Locale locale) throws java.text.ParseException {
		return formationRepository.getOne(Long.valueOf(id));
	}
}
