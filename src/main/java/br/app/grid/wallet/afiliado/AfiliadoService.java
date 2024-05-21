package br.app.grid.wallet.afiliado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.usuario.Usuario;
import br.app.grid.wallet.usuario.UsuarioUtil;

/**
 * <b>AfiliadoService</b><br>
 * Service para operações com a entidade Afiliado.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
@Service
public class AfiliadoService {

  @Autowired
  private AfiliadoRepository afiliadoRepository;
  @Autowired
  private AfiliadoUsuarioRepository afiliadoUsuarioRepository;

  /**
   * Retorna a lista de afiliados do usuario indicado.
   * 
   * @param usuario Usuário.
   * @return Lista de afiliados.
   */
  public List<Afiliado> getList(Usuario usuario) {
    if (Objects.isNull(usuario))
      return new ArrayList<>();
    return afiliadoRepository.getList(usuario);
  }

  /**
   * Seleciona o afiliado com o ID indicado.
   * 
   * @param idAfiliado ID do afiliado.
   * @param request Request.
   * @return Retorna o afiliado com o ID indicado.
   */
  public Afiliado selecionar(Integer idAfiliado, HttpServletRequest request) {
    Usuario usuario = UsuarioUtil.getUsuario(request);
    AfiliadoUsuario afiliadoUsuario = afiliadoUsuarioRepository.get(usuario.getId(), idAfiliado);
    Afiliado afiliado = (Objects.isNull(afiliadoUsuario) ? null : afiliadoUsuario.getAfiliado());
    UsuarioUtil.setAfiliado(afiliado, request);
    return afiliado;
  }

  /**
   * Retorna o afiliado padrão do usuário indicado.
   * 
   * @param usuario Usuário.
   * @return Afiliado padrão.
   */
  public Afiliado getAfiliadoPadrao(Usuario usuario) {
    if (Objects.isNull(usuario))
      return null;
    AfiliadoUsuario afiliadoUsuario = afiliadoUsuarioRepository.getPadrao(usuario.getId());
    return (Objects.isNull(afiliadoUsuario) ? null : afiliadoUsuario.getAfiliado());
  }

  /**
   * Retorna a lista de afiliados.
   * @return Lista de afiliados.
   */
  public List<Afiliado> getList() {
    return afiliadoRepository.getList();
  }

  /**
   * Retorna o afiliado com o ID indicado.
   * @param id ID do afiliado.
   * @return Afiliado.
   */
  public Afiliado get(Integer id) {
    return afiliadoRepository.get(id);
  }
}