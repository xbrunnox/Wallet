package br.app.grid.wallet.web;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.usuario.Usuario;
import br.app.grid.wallet.usuario.service.UsuarioService;
import br.app.grid.wallet.util.Md5Util;
import br.app.grid.wallet.web.request.CreateUserRequest;
import br.app.grid.wallet.web.request.UserLoginRequest;

/**
 * <b>UsuarioController</b><br>
 * Controlador web responsável pela entidade Usuario.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private HttpServletRequest request;

  @PostMapping("/registrar")
  public ModelAndView registrar(CreateUserRequest createUser) {
    usuarioService.registrar(Usuario.builder().email(createUser.getEmail().trim())
        .nome(createUser.getNome().trim()).senha(Md5Util.toMd5(createUser.getSenha())).build());
    return new ModelAndView("redirect:/login");
  }

  /**
   * Realiza o login de um usuário.
   * 
   * @param userLoginRequest Request de Login.
   * @return View.
   */
  @PostMapping("/login")
  public ModelAndView login(UserLoginRequest userLoginRequest) {
    // Autenticação.
    Usuario usuario = usuarioService.autenticar(userLoginRequest.getEmail(),
        Md5Util.toMd5(userLoginRequest.getSenha()), request);

    // Falha na autenticação.
    if (Objects.isNull(usuario)) {
      if (!Strings.isBlank(userLoginRequest.getRedirect()))
        return new ModelAndView("redirect:/login?redirect=" + userLoginRequest.getRedirect());
      return new ModelAndView("redirect:/login");
    }

    // Redirect após login.
    System.out.println("Login: " + usuario.getNome());
    if (!Strings.isBlank(userLoginRequest.getRedirect()))
      return new ModelAndView("redirect:" + userLoginRequest.getRedirect());
    return new ModelAndView("redirect:/");
  }

  
}
