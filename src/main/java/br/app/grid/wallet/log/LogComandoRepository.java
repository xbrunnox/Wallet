package br.app.grid.wallet.log;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogComandoRepository extends CrudRepository<LogComando, Long> {

	@Query("FROM LogComando lc WHERE lc.data = :data ORDER BY lc.dataHora DESC, lc.id")
	List<LogComando> getList(LocalDate data);

}
