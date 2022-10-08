package br.app.grid.wallet.licensa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.licensa.Licensa;

public interface LicensaRepository extends CrudRepository<Licensa, String> {

	@Query("FROM Licensa l WHERE l.corretora = :corretora AND l.conta = :conta")
	public Licensa get(String corretora, String conta);

}
