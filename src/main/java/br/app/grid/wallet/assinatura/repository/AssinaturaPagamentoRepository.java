package br.app.grid.wallet.assinatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.assinatura.AssinaturaPagamento;

@Repository
public interface AssinaturaPagamentoRepository extends CrudRepository<AssinaturaPagamento, Integer> {

	@Query("FROM AssinaturaPagamento ap WHERE ap.assinatura.id = :idAssinatura")
	List<AssinaturaPagamento> getList(int idAssinatura);

}
