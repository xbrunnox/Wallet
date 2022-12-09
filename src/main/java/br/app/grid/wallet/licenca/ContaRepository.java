package br.app.grid.wallet.licenca;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContaRepository extends CrudRepository<Conta, String> {

	@Query("FROM Conta l WHERE l.corretora = :corretora AND l.conta = :conta")
	public Conta get(String corretora, Integer conta);

	@Query("FROM ContaResultadoView cr ORDER BY cr.nome, cr.corretora")
	public List<ContaResultadoView> getListContaResultado();

	@Query("FROM Conta conta ORDER BY conta.id")
	public List<Conta> getList();

}
