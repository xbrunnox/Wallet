package br.app.grid.wallet.robo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.afiliado.Afiliado;

/**
 * <b>RoboRepository</b><br>
 * Repository para a entidade Robo.
 */
public interface RoboRepository extends CrudRepository<Robo, String> {

  @Query("FROM Robo r WHERE r.enabled = true")
  List<Robo> getListEnabled();

  @Query("FROM Robo r WHERE r.id = :expert")
  Robo getById(String expert);

  /**
   * Retorna a lista de automações habilitadas do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @return Lista de automações.
   */
  @Query("FROM Robo r WHERE r.enabled = true AND r.afiliado = :afiliado")
  List<Robo> getListEnabled(Afiliado afiliado);

  /**
   * Retorna o expert com o ID e afiliado indicados.
   * 
   * @param idExpert ID do expert.
   * @param afiliado Afiliado.
   * @return Automação.
   */
  @Query("FROM Robo r WHERE r.afiliado = :afiliado AND r.id = :idExpert")
  Robo get(String idExpert, Afiliado afiliado);

}
