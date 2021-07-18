package br.app.grid.wallet.posicao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.carteira.Carteira;

@Service
public class PosicaoService {
	
	@Autowired
	private PosicaoRepository repository;
	
	public List<Posicao>getList(Carteira carteira) {
		return repository.getList(carteira.getId());
	}

}
