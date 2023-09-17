package br.app.grid.wallet.ativo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AtivoRepository extends CrudRepository<Ativo, Integer> {

	@Query("FROM Ativo at WHERE at.codigo = :codigo")
	Ativo findByCodigo(@Param("codigo") String codigo);

	@Query("FROM Ativo")
	List<Ativo> getList();

	@Query("FROM Ativo at WHERE at.categoria.id = :idCategoria")
	List<Ativo> getListByCategoria(Integer idCategoria);

}
