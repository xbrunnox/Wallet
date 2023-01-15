package br.app.grid.wallet.backtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.app.grid.wallet.backtest.BacktestOperacao;

public interface BacktestOperacaoRepository extends CrudRepository<BacktestOperacao, Integer>{

	@Query("FROM BacktestOperacao bo WHERE bo.backtest.id = :idBacktest ORDER BY bo.dataSaida")
	List<BacktestOperacao> getList(@Param("idBacktest") Integer idBacktest);

}
