package br.app.grid.wallet.assinatura.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.view.AssinaturaExpertView;

@Repository
public interface AssinaturaExpertRepository extends CrudRepository<AssinaturaExpert, Integer> {

	@Query("FROM AssinaturaExpertView aev WHERE aev.conta = :conta AND aev.dataVencimento >= :data AND aev.ativado = true")
	public List<AssinaturaExpertView> getListAtivados(String conta, LocalDate data);

	@Query("FROM AssinaturaExpert ass WHERE ass.assinatura = :assinatura")
	public List<AssinaturaExpert> getList(Assinatura assinatura);

}
