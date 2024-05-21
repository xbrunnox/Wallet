package br.app.grid.wallet.afiliado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.usuario.Usuario;

/**
 * <b>AfiliadoUsuarioService</b><br>
 * Service para operações com a entidade AfiliadoUsuario.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
@Service
public class AfiliadoUsuarioService {

  @Autowired
  private AfiliadoUsuarioRepository repository;

  /**
   * Retorna a lista de AfiliadoUsuario do Usuário indicado.
   * 
   * @param usuario Usuário.
   * @return Lista de AfiliadoUsuario.
   */
  public List<AfiliadoUsuario> getList(Usuario usuario) {
    if (Objects.isNull(usuario)) {
      return new ArrayList<>();
    }
    return repository.getList(usuario);
  }
}
