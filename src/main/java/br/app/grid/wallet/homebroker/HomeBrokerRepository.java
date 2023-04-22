package br.app.grid.wallet.homebroker;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * HomeBrokerRepository<br>
 * Repository responsável pelas operações de banco de dados da entidade
 * HomeBroker.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 16 de abril de 2023.
 */
@Repository
public interface HomeBrokerRepository extends CrudRepository<HomeBroker, Integer> {

	@Query("FROM HomeBroker hb")
	public List<HomeBroker> getList();

}
