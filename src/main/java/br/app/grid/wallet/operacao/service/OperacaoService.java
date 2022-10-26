package br.app.grid.wallet.operacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.repository.OperacaoRepository;

@Service
public class OperacaoService {
	
	@Autowired
	private OperacaoRepository repository;
	
	public Operacao save(Operacao operacao) {
		repository.save(operacao);
		return operacao;
	}

}
