package br.app.grid.wallet.afiliado;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.usuario.Usuario;

/**
 * <b>AfiliadoRepository</b><br>
 * Repository para operações de banco de dados com a entidade Afiliado.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
public interface AfiliadoRepository extends CrudRepository<Afiliado, Integer> {

  /**
   * Retorna a lista de afiliados do usuário indicado.
   * 
   * @param usuario Usuário.
   * @return Lista de afiliados.
   */
  @Query("SELECT pu.afiliado FROM AfiliadoUsuario pu WHERE pu.usuario = :usuario")
  List<Afiliado> getList(Usuario usuario);

  /**
   * Retorna a lista de afiliados ordenadas pelo nome.
   * 
   * @return Lista de afiliados.
   */
  @Query("FROM Afiliado af ORDER BY af.nome")
  List<Afiliado> getList();

  /**
   * Retorna o afiliado com o ID indicado.
   * 
   * @param idAfiliado ID do afiliado.
   * @return Afiliado.
   */
  @Query("FROM Afiliado af WHERE af.id = :idAfiliado ORDER BY af.nome")
  Afiliado get(Integer idAfiliado);

}
