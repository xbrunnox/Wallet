package br.app.grid.wallet.robo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * <b>RoboRepository</b><br>
 * Repository para a entidade Robo.
 */
public interface RoboRepository extends CrudRepository<Robo, String> {

  @Query("FROM Robo r WHERE r.enabled = true")
  List<Robo> getListEnabled();

  @Query("FROM Robo r WHERE r.id = :expert")
  Robo getById(String expert);

}
