package br.app.grid.wallet.afiliado;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.usuario.Usuario;

/**
 * <b>AfiliadoUsuarioRepository</b><br>
 * Repository para operações de banco de dados com a entidade AfiliadoUsuario.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
public interface AfiliadoUsuarioRepository extends CrudRepository<AfiliadoUsuario, Integer> {

  /**
   * Retorna a lista de AfiliadoUsuario do usuário indicado.
   * 
   * @param usuario Usuário.
   * @return Lista de AfiliadoUsuario.
   */
  @Query("FROM AfiliadoUsuario pu WHERE pu.usuario = :usuario")
  List<AfiliadoUsuario> getList(Usuario usuario);

  /**
   * Retorna o AfiliadoUsuario.
   * 
   * @param idUsuario ID do Usuário.
   * @param idAfiliado ID do Afiliado.
   * @return AfiliadoUsuario.
   */
  @Query("FROM AfiliadoUsuario pu WHERE pu.usuario.id = :idUsuario AND pu.afiliado.id = :idAfiliado")
  AfiliadoUsuario get(Integer idUsuario, Integer idAfiliado);

  /**
   * Retorna o afiliado padrão do usuário indicado.
   * 
   * @param idUsuario ID do Usuário.
   * @return Afiliado padrão.
   */
  @Query("FROM AfiliadoUsuario pu WHERE pu.usuario.id = :idUsuario AND pu.padrao = true")
  AfiliadoUsuario getPadrao(Integer idUsuario);
}
