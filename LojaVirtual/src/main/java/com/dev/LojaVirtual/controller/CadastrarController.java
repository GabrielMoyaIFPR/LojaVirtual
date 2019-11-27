package com.dev.LojaVirtual.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;




@Controller
public class CadastrarController {
	
	
	
	@GetMapping("/cliente/cadastro")
	public ModelAndView login() {
		ModelAndView mv =  new ModelAndView("/cliente/cadastro");
		return mv;
	}
	
	
}
