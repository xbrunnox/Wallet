package br.app.grid.wallet.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.meta.PosicoesMT;
import br.app.grid.wallet.router.RouterService;

/**
 * <b>EndpointController</b><br>
 * Controlador Web responsável pela comunicação com os EndPoints.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@RestController
@RequestMapping("/endpoint")
public class EndpointController {

  @Autowired
  private EndpointService endpointService;

  @Autowired
  private RouterService routerService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping("/conexoes")
  public void conexoes() {

  }

  @GetMapping("/posicoes/{conta}")
  public ModelAndView posicoes(@PathVariable(name = "conta") String conta) {
    PosicoesMT posicoes = endpointService.getPosicoes(conta);

    ModelAndView modelAndView = new ModelAndView("endpoint/posicoes");
    modelAndView.addObject("posicoes", posicoes);
    return modelAndView;
  }

  @GetMapping("/status")
  public EndpointStatusResponse status() {
    return routerService.getStatus(request);
  }

}
