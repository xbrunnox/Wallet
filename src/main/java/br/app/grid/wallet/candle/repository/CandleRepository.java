package br.app.grid.wallet.candle.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.candle.Candle;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
public interface CandleRepository extends CrudRepository<Candle, Long> {

  @Query("FROM Candle cdl WHERE cdl.ativo = :ativo AND cdl.timeFrame = :timeFrame AND DATE(cdl.dataHora) = :data ORDER BY cdl.dataHora")
  List<Candle> getList(String ativo, Integer timeFrame, Date data);

  @Query(nativeQuery = true,
      value = "SELECT * FROM candle cdl WHERE cdl.ativo = :ativo AND cdl.time_frame = :timeFrame ORDER BY cdl.data_hora DESC LIMIT :resultados")
  List<Candle> getListUltimos(String ativo, Integer timeFrame, Integer resultados);

  @Query("FROM Candle cdl WHERE cdl.ativo = :ativo AND cdl.timeFrame = :timeFrame AND DATE(cdl.dataHora) >= :dataInicial "
      + "AND DATE(cdl.dataHora) <= :dataFinal ORDER BY cdl.dataHora")
  List<Candle> getList(String ativo, Integer timeFrame, Date dataInicial, Date dataFinal);

}
