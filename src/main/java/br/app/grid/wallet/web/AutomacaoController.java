package br.app.grid.wallet.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.usuario.UsuarioUtil;

/**
 * <b>AutomacaoController</b><br>
 * Controller responsável pelas operações Web da entidade Robo.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 18 de abril de 2023.
 */
@RestController
@RequestMapping("/automacao")
public class AutomacaoController {

  @Autowired
  private RoboService roboService;


  @Autowired
  private HttpServletRequest request;

  @GetMapping("/ativas")
  public List<Robo> getAtivas() {
    return roboService.getListEnabled();
  }

  @GetMapping("/listar-ativas")
  public List<Robo> getListAtivas() {
    System.out.println(request.getRequestURL());
    return roboService.getListEnabled(UsuarioUtil.getAfiliado(request));
  }

}
