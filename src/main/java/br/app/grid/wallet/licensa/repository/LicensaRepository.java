package br.app.grid.wallet.licensa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.licensa.Licenca;

public interface LicensaRepository extends CrudRepository<Licenca, String> {

	@Query("FROM Licenca l WHERE l.corretora = :corretora AND l.conta = :conta")
	public Licenca get(String corretora, Integer conta);

}
