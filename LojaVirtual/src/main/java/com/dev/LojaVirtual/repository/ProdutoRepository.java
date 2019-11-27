package com.dev.LojaVirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.LojaVirtual.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
