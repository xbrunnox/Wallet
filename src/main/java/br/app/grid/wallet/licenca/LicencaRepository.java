package br.app.grid.wallet.licenca;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LicencaRepository extends CrudRepository<Licenca, String> {

	@Query("FROM Licenca l WHERE l.corretora = :corretora AND l.conta = :conta")
	public Licenca get(String corretora, Integer conta);

}
