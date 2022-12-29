package br.app.grid.wallet.pagamento;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

	@Query("FROM Pagamento pag WHERE pag.associado = false ORDER BY pag.nome")
	List<Pagamento> getListNaoAssociados();

	@Query("FROM Pagamento pag ORDER BY pag.dataAtualizacao")
	List<Pagamento> getList();

	@Query("FROM Pagamento pag WHERE pag.id = :idPagamento")
	Pagamento get(Long idPagamento);

}