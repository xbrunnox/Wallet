package br.app.grid.wallet.licenca;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContaRepository extends CrudRepository<Conta, String> {

	@Query("FROM Conta l WHERE l.corretora = :corretora AND l.conta = :conta")
	public Conta get(String corretora, Integer conta);

}
