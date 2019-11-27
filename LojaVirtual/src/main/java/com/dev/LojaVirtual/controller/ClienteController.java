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
import com.dev.LojaVirtual.models.Cliente;
import com.dev.LojaVirtual.repository.CidadeRepository;
import com.dev.LojaVirtual.repository.ClienteRepository;



@Controller
public class ClienteController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping("/cliente/cadastrar")
	public ModelAndView cadastrar(Cliente cliente) {
		ModelAndView mv =  new ModelAndView("/cliente/cadastro");
		mv.addObject("cliente",cliente);
		List<Cidade> listaCidades = cidadeRepository.findAll();
		mv.addObject("cidades",listaCidades);
		return mv;
	}

	@PostMapping("/cliente/salvar")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(cliente);
		}
		cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));
		clienteRepository.saveAndFlush(cliente);
		
		return cadastrar(new Cliente());
	}
	


	@GetMapping("/cliente/editarCliente/{id}")
	public ModelAndView edit(@PathVariable("id") Long id) {
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		Cliente e = cliente.get();	
		
		return cadastrar(e);
	}
	

	
}
