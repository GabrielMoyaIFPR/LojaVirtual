package com.dev.LojaVirtual.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.CategoriaProduto;
import com.dev.LojaVirtual.models.Produto;
import com.dev.LojaVirtual.repository.CategoriaProdutoRepository;
import com.dev.LojaVirtual.repository.ProdutoRepository;



@Controller
public class ProdutoController {
	
	private static String caminhoImagens = "/Documents/Faculdade/2 ANO/Eclipse Spring/imagens/";
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	@GetMapping("/administrativo/produto/cadastrar")
	public ModelAndView cadastrar(Produto produto) {
		ModelAndView mv =  new ModelAndView("administrativo/produto/cadastro");
		mv.addObject("produto",produto);
		List<CategoriaProduto> listaCategorias = categoriaProdutoRepository.findAll();
		mv.addObject("categoria", listaCategorias);
		return mv;
	}

	@PostMapping("/administrativo/produto/salvar")
	public ModelAndView salvar(@Valid Produto produto, BindingResult result, @RequestParam("file")  MultipartFile arquivo) {
		
		if(result.hasErrors()) {
			return cadastrar(produto);
		}
		
		produtoRepository.saveAndFlush(produto);
		
		try {
			if(!arquivo.isEmpty()){
			byte[] bytes = arquivo.getBytes();
			Path caminho = Paths.get(caminhoImagens+String.valueOf(produto.getId())+arquivo.getOriginalFilename());
			Files.write(caminho, bytes);
			produto.setNomeImagem(String.valueOf(produto.getId())+arquivo.getOriginalFilename());
			produtoRepository.saveAndFlush(produto);			
			}
			}catch(IOException e){
			e.printStackTrace();
		}
		
		return cadastrar(new Produto());
	}
	
	@GetMapping("/administrativo/produto/listar")
	public ModelAndView buscarTodos() {
		
		ModelAndView mv = new ModelAndView("/administrativo/produto/lista");
		mv.addObject("produtos", produtoRepository.findAll());
		
		return mv;
	}
	
	@GetMapping("/cliente/produto/lista")
	public ModelAndView buscar() {
		
		ModelAndView mv = new ModelAndView("/cliente/lista");
		mv.addObject("produtos", produtoRepository.findAll());
		
		return mv;
	}
	
	@GetMapping("/administrativo/produto/editarProduto/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		Produto e = produto.get();	
		
		return cadastrar(e);
	}
	
	@GetMapping("/administrativo/produto/removerProduto/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		Produto e = produto.get();
		produtoRepository.delete(e);	
		
		return buscarTodos();
	}
	
	@GetMapping("/administrativo/produto/mostrarImagem/{imagem}")
	@ResponseBody
	public byte[] retornarImagem(@PathVariable("imagem") String imagem) throws IOException {
		
		File imagemArquivo = new File(caminhoImagens+imagem);
		if(imagem!=null || imagem.trim().length()>0) {

			return Files.readAllBytes(imagemArquivo.toPath());
		
	}
		return null;
	
}
}
