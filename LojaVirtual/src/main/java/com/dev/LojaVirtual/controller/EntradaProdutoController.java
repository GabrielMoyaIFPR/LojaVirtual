package com.dev.LojaVirtual.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.EntradaItens;
import com.dev.LojaVirtual.models.EntradaProduto;
import com.dev.LojaVirtual.models.Produto;
import com.dev.LojaVirtual.repository.EntradaItensRepository;
import com.dev.LojaVirtual.repository.EntradaProdutoRepository;
import com.dev.LojaVirtual.repository.FuncionarioRepository;
import com.dev.LojaVirtual.repository.ProdutoRepository;

@Controller
public class EntradaProdutoController {

	private List<EntradaItens> listaEntrada = new ArrayList();
	@Autowired
	private EntradaProdutoRepository entradaProdutoRepository;
	
	@Autowired
	private EntradaItensRepository entradaItensRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/administrativo/entrada/cadastrar")
	public ModelAndView cadastrar(EntradaProduto entrada, EntradaItens entradaItens) {
		ModelAndView mv =  new ModelAndView("administrativo/entrada/cadastro");
		mv.addObject("entrada",entrada);
		mv.addObject("listaEntradaItens", this.listaEntrada);
		mv.addObject("entradaItens",entradaItens);
		mv.addObject("listaFuncionarios", funcionarioRepository.findAll());
		mv.addObject("listaProdutos", produtoRepository.findAll());
		return mv;
	}

	@PostMapping("/administrativo/entrada/salvar")
	public ModelAndView salvar(String acao,EntradaProduto entrada, EntradaItens entradaItens) {
		
		System.out.println(acao);
		if(acao.equals("itens")) {
			this.listaEntrada.add(entradaItens);
		}else if(acao.equals("salvar")){
			entradaProdutoRepository.saveAndFlush(entrada);
			for(EntradaItens it : listaEntrada) {
				entradaItensRepository.saveAndFlush(it);
				Optional<Produto> prod = produtoRepository.findById(it.getProduto().getId());
				Produto produto = prod.get();
				produto.setQt_estoque(produto.getQt_estoque()+it.getQuantidade());
				produto.setPreco(it.getValorVenda());
				produtoRepository.saveAndFlush(produto);
				this.listaEntrada = new ArrayList<>();
			}
			return cadastrar(new EntradaProduto(),  new EntradaItens());

		}
		System.out.println(this.listaEntrada.size());
		return cadastrar(entrada,  new EntradaItens());
	}
	
	/*
	 * @GetMapping("/administrativo/estado/listar") public ModelAndView
	 * buscarTodos() {
	 * 
	 * ModelAndView mv = new ModelAndView("/administrativo/estado/lista");
	 * mv.addObject("estados", estadoRepository.findAll());
	 * 
	 * return mv; }
	 * 
	 * @GetMapping("/administrativo/estado/editarEstado/{id}") public ModelAndView
	 * edit(@PathVariable("id") Integer id) {
	 * 
	 * Optional<Estado> estado = estadoRepository.findById(id); Estado e =
	 * estado.get();
	 * 
	 * return cadastrar(e); }
	 * 
	 * @GetMapping("/administrativo/estado/removerEstado/{id}") public ModelAndView
	 * delete(@PathVariable("id") Integer id) {
	 * 
	 * Optional<Estado> estado = estadoRepository.findById(id); Estado e =
	 * estado.get(); estadoRepository.delete(e);
	 * 
	 * return buscarTodos(); }
	 */
}
