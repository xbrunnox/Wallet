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
	
	/**
	 * Retorna a lista de ativos de acordo com a categoria.
	 * @param idCategoria ID da categoria.
	 * @return Lista de ativos com a categoria indicada.
	 */
	public List<Ativo> getListByCategoria(Integer idCategoria) {
		return repository.getListByCategoria(idCategoria);
	}
	

}
