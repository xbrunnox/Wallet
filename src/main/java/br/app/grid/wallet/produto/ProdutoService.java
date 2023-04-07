package br.app.grid.wallet.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto getByNome(String produto) {
		return produtoRepository.getByNome(produto);
	}

}
