package com.dev.LojaVirtual.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.Cidade;
import com.dev.LojaVirtual.models.Estado;
import com.dev.LojaVirtual.repository.CidadeRepository;
import com.dev.LojaVirtual.repository.EstadoRepository;



@Controller
public class CidadeController {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@GetMapping("/administrativo/cidade/cadastrar")
	public ModelAndView cadastrar(Cidade cidade) {
		ModelAndView mv =  new ModelAndView("administrativo/cidade/cadastro");
		mv.addObject("cidade",cidade);
		List<Estado> listaEstados = estadoRepository.findAll();
		mv.addObject("estados",listaEstados);
		System.out.println("Resposta");
		return mv;
	}

	@PostMapping("/administrativo/cidade/salvar")
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(cidade);
		}
		cidadeRepository.saveAndFlush(cidade);
		
		return cadastrar(new Cidade());
	}
	
	@GetMapping("/administrativo/cidade/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/cidade/lista");
		mv.addObject("cidades", cidadeRepository.findAll());
		
		return mv;
	}

	@GetMapping("/administrativo/cidade/editarCidade/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id) {
		
		Optional<Cidade> cidade = cidadeRepository.findById(id);
		Cidade e = cidade.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/cidade/removerCidade/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {
		
		Optional<Cidade> cidade = cidadeRepository.findById(id);
		Cidade e = cidade.get();
		cidadeRepository.delete(e);	
		
		return buscarTodos();
	}
	
}
