package br.app.grid.wallet.ativo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AtivoRepository extends CrudRepository<Ativo, Integer> {

	@Query("FROM Ativo at WHERE at.codigo = :codigo")
	Ativo findByCodigo(@Param("codigo") String codigo);

}
