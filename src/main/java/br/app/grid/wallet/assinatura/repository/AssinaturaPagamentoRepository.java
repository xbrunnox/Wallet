package br.app.grid.wallet.assinatura.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;

public interface AssinaturaPagamentoRepository
    extends CrudRepository<AssinaturaPagamento, Integer> {

  @Query("FROM AssinaturaPagamento ap WHERE ap.assinatura.id = :idAssinatura")
  List<AssinaturaPagamento> getList(int idAssinatura);

  @Query("FROM AssinaturaPagamento ap WHERE ap.assinatura.conta.id = :conta ORDER BY ap.pagamento.dataAtualizacao")
  List<AssinaturaPagamento> getListPagamentos(String conta);

  @Query("FROM AssinaturaPagamento ap WHERE ap.assinatura.id = :idAssinatura ORDER BY ap.pagamento.dataAtualizacao")
  List<AssinaturaPagamento> getListPagamentos(int idAssinatura);

  @Query("FROM AssinaturaPagamento ap WHERE ap.id = :idPagamento")
  AssinaturaPagamento getById(Integer idPagamento);

}
