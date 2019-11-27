package com.dev.LojaVirtual.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeClienteController {
	
	@GetMapping("/cliente")
	public String acessarPrincipal() {
		return "cliente/home";
	}

}
