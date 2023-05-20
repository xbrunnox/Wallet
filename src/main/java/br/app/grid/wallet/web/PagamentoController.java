package br.app.grid.wallet.web;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.exception.BusinessException;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.web.request.AssociarTratarPagamentoRequest;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping("/get/{idPagamento}")
  public Pagamento get(@PathVariable(name = "idPagamento") Long id) {
    if (!UsuarioUtil.isLogged(request)) {
      throw new RuntimeException("User not logged.");
    }
    return pagamentoService.get(id);
  }

  @GetMapping("/recebido")
  public ModelAndView recebido() {
    if (!UsuarioUtil.isLogged(request)) {
      return new ModelAndView("redirect:/login");
    }
    List<Pagamento> pagamentos = pagamentoService.getList();
    Collections.reverse(pagamentos);
    ModelAndView view = new ModelAndView("pagamento/recebido");
    view.addObject("pagamentosList", pagamentos);
    return view;
  }

  @GetMapping("/identificar/{idPagamento}")
  public void identificar(@PathVariable(name = "idPagamento") Long idPagamento) {
    System.out.println("Identificando pagamento");
    assinaturaService.identificarPagamento(idPagamento);
  }

  /**
   * Realiza a associação e tratamento de pagamento.
   * 
   * @param associarRequest Request de associação.
   * @return Associação do pagamento.
   */
  @PostMapping("/associar-tratar")
  public AssinaturaPagamento associar(@RequestBody AssociarTratarPagamentoRequest associarRequest) {
    if (!UsuarioUtil.isLogged(request)) {
      throw new BusinessException("O usuário não está logado.");
    }
    return assinaturaService.associarTratarPagamento(associarRequest);
  }

}
