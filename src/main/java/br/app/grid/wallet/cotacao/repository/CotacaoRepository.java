package br.app.grid.wallet.cotacao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.cotacao.Cotacao;

@Repository
public interface CotacaoRepository extends CrudRepository<Cotacao, Integer> {

	@Query("FROM Cotacao c WHERE c.ativo.id = :idAtivo ORDER BY c.data DESC")
	Cotacao findFirstByIdAtivo(@Param("idAtivo") Integer idAtivo);

}
