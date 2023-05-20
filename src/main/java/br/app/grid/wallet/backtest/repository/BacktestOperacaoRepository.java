package br.app.grid.wallet.backtest.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoResultadoDiaVO;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoResultadoMesVO;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoVO;

public interface BacktestOperacaoRepository extends CrudRepository<BacktestOperacao, Integer> {

  @Query("FROM BacktestOperacao bo WHERE bo.backtest.id = :idBacktest ORDER BY bo.dataSaida")
  List<BacktestOperacao> getList(@Param("idBacktest") Integer idBacktest);

  @Query("FROM BacktestOperacaoVO bo WHERE bo.idBacktest = :idBacktest AND bo.dataEntrada >= :dataInicial "
      + "AND bo.dataEntrada <= :dataFinal ORDER BY bo.dataHoraEntrada")
  List<BacktestOperacaoVO> getListVO(Integer idBacktest, LocalDate dataInicial, LocalDate dataFinal);

  @Query("FROM BacktestOperacaoResultadoDiaVO bo WHERE bo.id = :idBacktest")
  List<BacktestOperacaoResultadoDiaVO> getListResultadosDiarios(Integer idBacktest);
  
  @Query("FROM BacktestOperacaoResultadoMesVO bo WHERE bo.id = :idBacktest")
  List<BacktestOperacaoResultadoMesVO> getListResultadosMensais(Integer idBacktest);

}
