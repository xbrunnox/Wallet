package br.app.grid.wallet.trade;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Long> {

	@Query("FROM Trade tr WHERE tr.conta.id = :conta")
	List<Trade> getList(String conta);

//	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND op.expert.id = :expert "
//			+ "AND op.ativo.codigo = :ativo AND op.tipo = :tipo AND DATE(op.data) = :date")
	@Query("SELECT tr FROM Trade tr WHERE DATE(tr.dataSaida) = :data")
	List<Trade> getList(Date data);

}
