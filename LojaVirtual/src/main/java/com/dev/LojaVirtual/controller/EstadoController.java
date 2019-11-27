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

import com.dev.LojaVirtual.models.Estado;
import com.dev.LojaVirtual.repository.EstadoRepository;

@Controller
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@GetMapping("/administrativo/estado/cadastrar")
	public ModelAndView cadastrar(Estado estado) {
		ModelAndView mv =  new ModelAndView("administrativo/estado/cadastro");
		mv.addObject("estado",estado);
		return mv;
	}

	@PostMapping("/administrativo/estado/salvar")
	public ModelAndView salvar(@Valid Estado estado, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(estado);
		}
		estadoRepository.saveAndFlush(estado);
		
		return cadastrar(new Estado());
	}
	
	@GetMapping("/administrativo/estado/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/estado/lista");
		mv.addObject("estados", estadoRepository.findAll());
		
		return mv;
	}
	
	@GetMapping("/administrativo/estado/editarEstado/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id) {
		
		Optional<Estado> estado = estadoRepository.findById(id);
		Estado e = estado.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/estado/removerEstado/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {
		
		Optional<Estado> estado = estadoRepository.findById(id);
		Estado e = estado.get();
		estadoRepository.delete(e);	
		
		return buscarTodos();
	}
}
