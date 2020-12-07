package fr.orsys.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
	@RequestMapping(value="/403")
	public String accessDenid() {
	return "403";
	}
	@RequestMapping(value="/login")
	public String login() {
	return "login";
	}

}
