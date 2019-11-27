package com.dev.LojaVirtual.controller;

import java.util.List; 
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.Cidade;
import com.dev.LojaVirtual.models.Funcionario;
import com.dev.LojaVirtual.repository.CidadeRepository;
import com.dev.LojaVirtual.repository.FuncionarioRepository;

@Controller
public class FuncionarioController {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@GetMapping("/administrativo/funcionario/cadastrar")
	public ModelAndView cadastrar(Funcionario funcionario) {
		ModelAndView mv =  new ModelAndView("administrativo/funcionario/cadastro");
		mv.addObject("funcionario",funcionario);
		List<Cidade> listaCidade = cidadeRepository.findAll();
		mv.addObject("cidades",listaCidade);
		return mv;
	}

	@PostMapping("/administrativo/funcionario/salvar")
	public ModelAndView salvar(@Valid Funcionario funcionario, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(funcionario);
		}
		System.out.println(funcionario.getSenha());
		funcionario.setSenha(new BCryptPasswordEncoder().encode(funcionario.getSenha()));
		System.out.println(funcionario.getSenha());
		funcionarioRepository.saveAndFlush(funcionario);
		
		return cadastrar(new Funcionario());
	}

	@GetMapping("/administrativo/funcionario/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/funcionario/lista");
		mv.addObject("funcionarios", funcionarioRepository.findAll());
		
		return mv;
	}

	@GetMapping("/administrativo/funcionario/editarFuncionario/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id) {
		
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		Funcionario e = funcionario.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/funcionario/removerFuncionario/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {
		
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		Funcionario e = funcionario.get();
		funcionarioRepository.delete(e);	
		
		return buscarTodos();
	}
	
}
