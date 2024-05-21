package br.app.grid.wallet.usuario;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.afiliado.AfiliadoService;

public class UsuarioUtil {

  public static boolean isLogged(HttpServletRequest request) {
    try {
      Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
      if (usuario == null)
        return false;
      return usuario.isAtivo();
    } catch (Exception e) {
      return false;
    }
  }

  public static Usuario getUsuario(HttpServletRequest request) {
    try {
      Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
      if (usuario == null)
        return null;
      return usuario;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Define o afiliado selecionado na sessão.
   * 
   * @param afiliado Afiliado.
   * @param request Request.
   */
  public static void setAfiliado(Afiliado afiliado, HttpServletRequest request) {
    request.getSession().setAttribute("afiliado", afiliado);
  }

  /**
   * Retorna o afiliado selecionado na sessão.
   * 
   * @param request Request.
   * @return Afiliado.
   */
  public static Afiliado getAfiliado(HttpServletRequest request) {
    try {
      Afiliado afiliado = (Afiliado) request.getSession().getAttribute("afiliado");
      if (Objects.isNull(afiliado))
        return null;
      return afiliado;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Define o usuário logado no sistema.
   * 
   * @param usuario Usuário.
   * @param request Request.
   */
  public static void setUsuario(Usuario usuario, HttpServletRequest request) {
    request.getSession().setAttribute("usuario", usuario);
  }

  public static String getTituloAplicativo(AfiliadoService afiliadoService,
      HttpServletRequest request) {
    String titulo = (String) request.getSession().getAttribute("titulo");
    if (Objects.isNull(titulo)) {
      List<Afiliado> afiliados = afiliadoService.getList();
      String url = request.getRequestURL().toString();
      for (Afiliado afiliado : afiliados) {
        if (!Strings.isBlank(afiliado.getUrl())) {
          if (url.contains(afiliado.getUrl())) {
            titulo = afiliado.getNome();
            request.getSession().setAttribute("titulo", titulo);
            break;
          }
        }
      }
      if (Objects.isNull(titulo)) {
        titulo = "Trading Now";
      }
    }
    return titulo;
  }

}
