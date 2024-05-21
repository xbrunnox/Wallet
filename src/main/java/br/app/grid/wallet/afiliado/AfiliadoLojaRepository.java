package br.app.grid.wallet.afiliado;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * <b>AfiliadoLojaRepository</b><br>
 * Repository para operações de banco de dados com a entidade AfiliadoLoja.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 03 de março de 2024.
 */
public interface AfiliadoLojaRepository extends CrudRepository<AfiliadoLoja, Integer> {

  @Query("FROM AfiliadoLoja al WHERE al.idLoja = :idLoja")
  public AfiliadoLoja getByIdLoja(String idLoja);

}
