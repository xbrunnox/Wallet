package br.app.grid.wallet.usuario.service;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.afiliado.AfiliadoService;
import br.app.grid.wallet.usuario.Usuario;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private AfiliadoService afiliadoService;

  public void registrar(Usuario usuario) {
    usuarioRepository.save(usuario);
  }

  /**
   * Realiza a autenticação do Usuário.
   * 
   * @param email Email.
   * @param senha Senha.
   * @param request Http Request.
   * @return Usuário após autenticação.
   */
  public Usuario autenticar(String email, String senha, HttpServletRequest request) {
    Usuario usuario = usuarioRepository.autenticar(email, senha);
    UsuarioUtil.setUsuario(usuario, request);
    if (!Objects.isNull(usuario)) {
      Afiliado afiliadoPadrao = afiliadoService.getAfiliadoPadrao(usuario);
      UsuarioUtil.setAfiliado(afiliadoPadrao, request);
    }
    return usuario;
  }

  /**
   * Realiza o logout.
   * 
   * @param request Http Request.
   */
  public void logout(HttpServletRequest request) {
    UsuarioUtil.setUsuario(null, request);
    UsuarioUtil.setAfiliado(null, request);
  }

}
