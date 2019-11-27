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

import com.dev.LojaVirtual.models.PermissoesFuncionario;
import com.dev.LojaVirtual.repository.FuncionarioRepository;
import com.dev.LojaVirtual.repository.PapelRepository;
import com.dev.LojaVirtual.repository.PermissoesFuncionarioRepository;



@Controller
public class PermissoesFuncionarioController {
	
	@Autowired
	private PermissoesFuncionarioRepository permissoesFuncionarioRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@GetMapping("/administrativo/permissoesFuncionario/cadastrar")
	public ModelAndView cadastrar(PermissoesFuncionario permissoesFuncionario) {
		ModelAndView mv =  new ModelAndView("/administrativo/permissoesFuncionario/cadastro");
		mv.addObject("permissoes",permissoesFuncionario);
		mv.addObject("funcionarios",funcionarioRepository.findAll());
		mv.addObject("papeis",papelRepository.findAll());	
		return mv;
	}

	@PostMapping("/administrativo/permissoesFuncionario/salvar")
	public ModelAndView salvar(@Valid PermissoesFuncionario permissoesFuncionario, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(permissoesFuncionario);
		}
		permissoesFuncionarioRepository.saveAndFlush(permissoesFuncionario);
		
		return cadastrar(new PermissoesFuncionario());
	}
	
	@GetMapping("/administrativo/permissoesFuncionario/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/permissoesFuncionario/lista");
		mv.addObject("permissoes", permissoesFuncionarioRepository.findAll());
		
		return mv;
	}

	@GetMapping("/administrativo/permissoesFuncionario/editarPermissoesFuncionario/{id}")
	public ModelAndView edit(@PathVariable("id") long id) {
		
		Optional<PermissoesFuncionario> permissoes = permissoesFuncionarioRepository.findById(id);
		PermissoesFuncionario e = permissoes.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/permissoesFuncionario/removerPermissoesFuncionario/{id}")
	public ModelAndView delete(@PathVariable("id") long id) {
		
		Optional<PermissoesFuncionario> permissoes = permissoesFuncionarioRepository.findById(id);
		PermissoesFuncionario e = permissoes.get();
		permissoesFuncionarioRepository.delete(e);	
		
		return buscarTodos();
	}
	
}
