package com.dev.LojaVirtual.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/administrativo")
	public String acessarPrincipal() {
		return "administrativo/home";
	}

}
