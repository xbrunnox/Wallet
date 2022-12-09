package br.app.grid.wallet.log;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogOnlineRepository extends CrudRepository<LogOnline, Long> {

	@Query("FROM LogOnline lo ORDER BY lo.horario")
	List<LogOnline> getList();

	@Query(value = "SELECT lo FROM LogOnline lo ORDER BY lo.horario LIMIT 288", nativeQuery = true)
	List<LogOnline> getList24Horas();
	
	List<LogOnline> findTop288ByOrderByHorarioDesc();

}
