package br.app.grid.wallet.backtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.backtest.Backtest;

public interface BacktestRepository extends CrudRepository<Backtest, Integer>{

	@Query("FROM Backtest")
	List<Backtest> getList();

}
