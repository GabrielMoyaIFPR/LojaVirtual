package com.dev.LojaVirtual.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.CategoriaProduto;
import com.dev.LojaVirtual.repository.CategoriaProdutoRepository;

@Controller
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	@GetMapping("/administrativo/categoriaProduto/cadastrar")
	public ModelAndView cadastrar(CategoriaProduto categoriaProduto) {
		ModelAndView mv = new ModelAndView("administrativo/categoriaProduto/cadastro");
		mv.addObject("categoriaProduto", categoriaProduto);
		List<CategoriaProduto> listaCategoriaProdutos = categoriaProdutoRepository.findAll();
		mv.addObject("categoriaProdutos", listaCategoriaProdutos);
		return mv;
	}

	/*
	 * @RequestMapping(value="/administrativo/categoriaProduto/salvar", method =
	 * RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public @ResponseBody CategoriaProduto
	 * salvar(@RequestBody CategoriaProduto categoriaProduto) {
	 * System.out.println("Salvar");
	 * categoriaProdutoRepository.saveAndFlush(categoriaProduto); return
	 * categoriaProduto; }
	 */

	@PostMapping("/administrativo/categoriaProduto/salvar")
	public ModelAndView salvar(@Valid CategoriaProduto categoriaProduto, BindingResult result) {

		if (result.hasErrors()) {
			return cadastrar(categoriaProduto);
		}
		categoriaProdutoRepository.saveAndFlush(categoriaProduto);

		return cadastrar(new CategoriaProduto());
	}

	@GetMapping("/administrativo/categoriaProduto/listar")
	public ModelAndView buscarTodos() {

		ModelAndView mv = new ModelAndView("/administrativo/categoriaProduto/lista");
		mv.addObject("categoriaProdutos", categoriaProdutoRepository.findAll());

		return mv;
	}

	@GetMapping("/administrativo/categoriaProduto/editarCategoriaProduto/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id) {

		Optional<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.findById(id);
		CategoriaProduto e = categoriaProduto.get();

		return cadastrar(e);
	}

	@GetMapping("/administrativo/categoriaProduto/removerCategoriaProduto/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {

		Optional<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.findById(id);
		CategoriaProduto e = categoriaProduto.get();
		categoriaProdutoRepository.delete(e);

		return buscarTodos();
	}

}
