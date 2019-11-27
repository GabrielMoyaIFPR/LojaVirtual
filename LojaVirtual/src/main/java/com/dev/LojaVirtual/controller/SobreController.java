package com.dev.LojaVirtual.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;




@Controller
public class SobreController {
	
	
	@GetMapping("/sobre")
	public ModelAndView cadastrar() {
		ModelAndView mv =  new ModelAndView("/cliente/sobre");
		return mv;
	}
	
}
