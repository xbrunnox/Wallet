package br.app.grid.wallet.delta.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.delta.Delta;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 16 de janeiro de 2022.
 */
@Repository
public interface DeltaRepository extends CrudRepository<Delta, Integer> {

	@Query("FROM Delta d ORDER BY d.data DESC")
	List<Delta> getList(Pageable page);

}
