package br.app.grid.wallet.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.afiliado.AfiliadoService;

/**
 * <b>AfiliadoController</b><br>
 * Controlador WEB responsável pela entidade Afiliado.
 */
@RestController
@RequestMapping("/afiliado")
public class AfiliadoController {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private AfiliadoService afiliadoService;

  /**
   * Seleciona o parceiro.
   * 
   * @param idAfiliado ID do afiliado.
   * @return Afiliado selecionado.
   */
  @GetMapping("/selecionar/{idAfiliado}")
  public Afiliado selecionar(@PathVariable(name = "idAfiliado") Integer idAfiliado) {
    Afiliado afiliado = afiliadoService.selecionar(idAfiliado, request);
    return afiliado;
  }

}
