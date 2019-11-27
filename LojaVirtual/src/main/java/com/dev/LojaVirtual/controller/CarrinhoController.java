package com.dev.LojaVirtual.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.LojaVirtual.models.Cliente;
import com.dev.LojaVirtual.models.Compra;
import com.dev.LojaVirtual.models.ItensCompra;
import com.dev.LojaVirtual.models.Produto;
import com.dev.LojaVirtual.repository.ClienteRepository;
import com.dev.LojaVirtual.repository.CompraRepository;
import com.dev.LojaVirtual.repository.ItensCompraRepository;
import com.dev.LojaVirtual.repository.ProdutoRepository;

@Controller
public class CarrinhoController {

	private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();
	private List<Compra> compras = new ArrayList<Compra>();
	private Compra compra = new Compra();
	private Cliente cliente;
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ItensCompraRepository itensCompraRepository;

	private void calcularTotal() {
		compra.setValorTotal(0.);
		for (ItensCompra it : itensCompra) {
			compra.setValorTotal(compra.getValorTotal() + it.getValorTotal());
		}
	}

	private void buscarUsuarioLogado() {
		Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
		if (!(autenticado instanceof AnonymousAuthenticationToken)) {
			String email = autenticado.getName();
			cliente = clienteRepository.buscarClienteEmail(email).get(0);
		}
	}

	@GetMapping("/carrinho")
	public ModelAndView chamarCarrinho() {
		ModelAndView mv = new ModelAndView("cliente/carrinho");
		mv.addObject("listaItens", itensCompra);
		calcularTotal();
		mv.addObject("compra", compra);
		mv.addObject("listaItens", itensCompra);
		return mv;
	}

	@GetMapping("/finalizar")
	public ModelAndView finalizarCompra() {
		buscarUsuarioLogado();
		ModelAndView mv = new ModelAndView("cliente/finalizar");
		mv.addObject("listaItens", itensCompra);
		calcularTotal();
		mv.addObject("compra", compra);
		mv.addObject("cliente", cliente);
		return mv;
	}

	@GetMapping("/removerProduto/{id}")
	public String removerProdutoCarrinho(@PathVariable Integer id) {

		for (ItensCompra it : itensCompra) {
			if (it.getProduto().getId().equals(id)) {
				itensCompra.remove(it);
				it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				break;
			}
		}

		return "redirect:/carrinho";
	}

	@PostMapping("/finalizar/confirmar")
	public ModelAndView confirmarCompra(String formaPagamento) {
		ModelAndView mv = new ModelAndView("cliente/mensagemFinalizou");
		compra.setCliente(cliente);
		compra.setFormaPagamento(formaPagamento);
		compraRepository.saveAndFlush(compra);
		mv.addObject("compras", compraRepository.findAll());

		for (ItensCompra c : itensCompra) {
			c.setCompra(compra);
			itensCompraRepository.saveAndFlush(c);
		}
		itensCompra = new ArrayList<>();
		compra = new Compra();
		return mv;
	}

	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public String alterarQuantidade(@PathVariable Integer id, @PathVariable Integer acao) {

		for (ItensCompra it : itensCompra) {
			if (it.getProduto().getId().equals(id)) {
				if (acao.equals(1)) {
					it.setValorTotal(0.);
					it.setQuantidade(it.getQuantidade() + 1);
					it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));

				} else if (acao == 0) {
					it.setQuantidade(it.getQuantidade() - 1);
					it.setValorTotal(0.);
					it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				}
				break;
			}
		}
		return "redirect:/carrinho";
	}

	@GetMapping("/adicionarCarrinho/{id}")
	public String adicionarCarrinho(@PathVariable Integer id) {
		Optional<Produto> prod = produtoRepository.findById(id);
		Produto produto = prod.get();

		int controle = 0;
		for (ItensCompra it : itensCompra) {
			if (it.getProduto().getId().equals(produto.getId())) {
				it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				it.setQuantidade(it.getQuantidade() + 1);
				controle = 1;
				break;
			}
		}
		if (controle == 0) {
			ItensCompra item = new ItensCompra();
			item.setProduto(produto);
			item.setValorUnitario(produto.getPreco());
			item.setQuantidade(item.getQuantidade() + 1);
			item.setValorTotal(item.getValorTotal() + (item.getQuantidade() * item.getValorUnitario()));
			itensCompra.add(item);
		}

		return "redirect:/carrinho";

	}

}
