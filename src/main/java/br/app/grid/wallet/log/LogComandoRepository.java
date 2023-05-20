package br.app.grid.wallet.log;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LogComandoRepository extends CrudRepository<LogComando, Long> {

  @Query("FROM LogComando lc WHERE lc.data = :data ORDER BY lc.dataHora DESC, lc.id")
  List<LogComando> getList(LocalDate data);


  @Query("FROM LogComando lc WHERE (:dataInicial IS NULL OR lc.data >= :dataInicial) "
      + "AND (:dataFinal IS NULL OR lc.data <= :dataFinal) "
      + "AND ((:conta1 IS NULL AND :conta2 IS NULL) OR (:conta1 IS NOT NULL AND lc.conta = :conta1) OR (:conta2 IS NOT NULL AND lc.conta = :conta2))"
      + "AND (:conteudo IS NULL OR lc.comando LIKE :conteudo) "
      + "ORDER BY lc.dataHora DESC, lc.id")
  List<LogComando> getList(LocalDate dataInicial, LocalDate dataFinal, String conta1, String conta2,
      String conteudo);

}
