package br.app.grid.wallet.ativo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtivoService {

	@Autowired
	private AtivoRepository repository;

	public Ativo get(String codigo) {
		return repository.findByCodigo(codigo);
	}

	public void salvar(Ativo ativo) {
		repository.save(ativo);
	}

	public List<Ativo> getList() {
		return repository.getList();
	}

}
