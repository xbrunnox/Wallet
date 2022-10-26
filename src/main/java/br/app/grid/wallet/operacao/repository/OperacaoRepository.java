package br.app.grid.wallet.operacao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.operacao.Operacao;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 20 de dezembro de 2022.
 */
@Repository
public interface OperacaoRepository extends CrudRepository<Operacao, Integer> {

	@Query("FROM Operacao d ORDER BY d.dataDeEntrada DESC")
	List<Operacao> getList(Pageable page);

}
