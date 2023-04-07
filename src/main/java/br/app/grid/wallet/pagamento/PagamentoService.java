package br.app.grid.wallet.pagamento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository repository;
	
	public void gravar(Pagamento pagamento) {
		repository.save(pagamento);
	}

	public List<Pagamento> getListNaoAssociados() {
		return repository.getListNaoAssociados();
	}

	public List<Pagamento> getList() {
		return repository.getList();
	}

	public Pagamento get(Long id) {
		return repository.get(id);
	}

}
