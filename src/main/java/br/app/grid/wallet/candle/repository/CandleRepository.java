package br.app.grid.wallet.candle.repository;

import java.time.LocalDateTime;
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

  @Query("FROM Candle cdl WHERE cdl.ativo = :ativo AND cdl.dataHora = :dateTime AND cdl.timeFrame = :timeFrame")
  Candle get(String ativo, LocalDateTime dateTime, Integer timeFrame);

  @Query("FROM Candle cdl WHERE cdl.id = :id")
  Candle get(Long id);

  @Query(nativeQuery = true,
      value = "SELECT * FROM candle cdl WHERE cdl.ativo = :ativo ORDER BY cdl.data_hora DESC LIMIT 1")
  Candle getUltimoPreco(String ativo);

  @Query(nativeQuery = true,
      value = "SELECT * FROM candle cdl WHERE cdl.ativo = :ativo AND cdl.time_frame = :timeFrame ORDER BY cdl.data_hora DESC LIMIT 1")
  Candle getUltimoCandle(String ativo, Integer timeFrame);

}
