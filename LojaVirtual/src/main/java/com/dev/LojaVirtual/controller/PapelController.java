package com.dev.LojaVirtual.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.Papel;
import com.dev.LojaVirtual.repository.PapelRepository;



@Controller
public class PapelController {
	
	@Autowired
	private PapelRepository papelRepository;
	
	
	@GetMapping("/administrativo/papel/cadastrar")
	public ModelAndView cadastrar(Papel papel) {
		ModelAndView mv =  new ModelAndView("administrativo/papel/cadastro");
		mv.addObject("papel",papel);
		return mv;
	}

	@PostMapping("/administrativo/papel/salvar")
	public ModelAndView salvar(@Valid Papel papel, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(papel);
		}
		papelRepository.saveAndFlush(papel);
		
		return cadastrar(new Papel());
	}
	
	@GetMapping("/administrativo/papel/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/papel/lista");
		mv.addObject("papeis", papelRepository.findAll());
		
		return mv;
	}

	@GetMapping("/administrativo/papel/editarPapel/{id}")
	public ModelAndView edit(@PathVariable("id") long id) {
		
		Optional<Papel> papel = papelRepository.findById(id);
		Papel e = papel.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/papel/removerPapel/{id}")
	public ModelAndView delete(@PathVariable("id") long id) {
		
		Optional<Papel> papel = papelRepository.findById(id);
		Papel e = papel.get();
		papelRepository.delete(e);	
		
		return buscarTodos();
	}
	
}
