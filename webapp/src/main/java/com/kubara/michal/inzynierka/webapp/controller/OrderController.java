package com.kubara.michal.inzynierka.webapp.controller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.service.ExpertService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private ExpertService expertService;
	
	@Autowired
	private MessageSource messages;

	@GetMapping("/{expertId}")
	public String showExpertOrderCalendar(@PathVariable("expertId") long expertId, @RequestHeader("Referer") String refererUrl, Model model, WebRequest request) {
		Optional<User> expert = expertService.findById(expertId);
		
		model.addAttribute("backUrl", refererUrl);
		
		if(!expert.isPresent()) {
			Locale locale = request.getLocale();
			model.addAttribute("errorMsg", messages.getMessage("order.message.userNotFound", null, locale));
			return "/order/orderError";
		} else if(expert.orElse(new User()).getRoles().stream().noneMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			Locale locale = request.getLocale();
			//userNotFound żeby nie pokazywać ciekawskim, że user o danym id nie ma uprawnień experta
			model.addAttribute("errorMsg", messages.getMessage("order.message.userNotFound", null, locale));
			return "/order/orderError";
		}
		
		model.addAttribute("expert", expert.orElse(new User()));
		
		return "/order/order";
		
	}
	
}
