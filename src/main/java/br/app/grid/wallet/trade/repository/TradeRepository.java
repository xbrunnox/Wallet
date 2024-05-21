package br.app.grid.wallet.trade.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.vo.TradeVO;

/**
 * <b>TradeRepository</b><br>
 * Repository responsável pelas operações da entidade Trade.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
public interface TradeRepository extends CrudRepository<Trade, Long> {

  @Query("FROM Trade tr WHERE tr.conta.id = :conta")
  List<Trade> getList(String conta);

  // @Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND op.expert.id = :expert "
  // + "AND op.ativo.codigo = :ativo AND op.tipo = :tipo AND DATE(op.data) = :date")
  @Query("SELECT tr FROM Trade tr WHERE DATE(tr.dataSaida) = :data")
  List<Trade> getList(Date data);

  @Query("SELECT tr FROM Trade tr WHERE tr.conta.id = :conta AND DATE(tr.dataSaida) = :data")
  List<Trade> getList(String conta, Date data);

  @Query("FROM Trade tr WHERE tr.expert.id = :expert")
  List<Trade> getListByExpert(String expert);

  @Query("SELECT tr FROM TradeVO tr WHERE (:idAfiliado IS NULL OR tr.idAfiliado= :idAfiliado) "
      + "AND (:conta IS NULL OR tr.conta = :conta) AND DATE(tr.dataSaida) = :data")
  List<TradeVO> getListVO(Integer idAfiliado, String conta, Date data);

}
