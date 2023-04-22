package br.app.grid.wallet.robo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoboRepository extends CrudRepository<Robo, String> {

	@Query("FROM Robo r WHERE r.enabled = true")
	List<Robo> getListEnabled();

}
