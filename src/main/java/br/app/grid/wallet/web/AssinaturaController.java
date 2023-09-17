package br.app.grid.wallet.web;

import java.util.List;
import java.util.Objects;
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
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpertsResponse;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaView;
import br.app.grid.wallet.exception.BusinessException;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.converter.AssinaturaPagamentoResponseConverter;
import br.app.grid.wallet.util.converter.AssinaturaResponseConverter;
import br.app.grid.wallet.web.request.AdicionarExpertAssinaturaRequest;
import br.app.grid.wallet.web.request.AlterarEmailRequest;
import br.app.grid.wallet.web.request.AlterarVencimentoRequest;
import br.app.grid.wallet.web.request.ExcluirAssinaturaPagamentoRequest;
import br.app.grid.wallet.web.response.AssinaturaPagamentoResponse;
import br.app.grid.wallet.web.response.AssinaturaResponse;

@RestController
@RequestMapping("/assinatura")
public class AssinaturaController {

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private ContaService contaService;

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private RouterService routerService;

  /**
   * Realiza a adição de um Expert na Assinatura.
   * 
   * @param request Dados do expert.
   */
  @PostMapping("/adicionar-expert")
  public void adicionarExpert(
      @RequestBody AdicionarExpertAssinaturaRequest adicionarExpertRequest) {
    assinaturaService.adicionarExpert(adicionarExpertRequest);
  }

  @GetMapping("/experts/{conta}")
  public AssinaturaExpertsResponse experts(@PathVariable("conta") String conta) {
    return assinaturaService.getExpertsAtivos(conta);
  }

  @GetMapping("/iniciarAtivacao/{conta}")
  public ModelAndView iniciarAtivacao(@PathVariable("conta") String idConta) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");

    Conta conta = contaService.get(idConta);
    List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();

    ModelAndView view = new ModelAndView("assinatura/iniciar_ativacao");
    view.addObject("conta", conta);
    view.addObject("pagamentosList", pagamentos);
    return view;
  }

  @GetMapping("/associarPagamento/{idAssinatura}")
  public ModelAndView associarPagamento(@PathVariable("idAssinatura") Integer idAssinatura) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");

    Assinatura assinatura = assinaturaService.get(idAssinatura);

    List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();

    ModelAndView view = new ModelAndView("assinatura/associar_pagamento");
    view.addObject("assinatura", assinatura);
    view.addObject("conta", assinatura.getConta());
    view.addObject("pagamentosList", pagamentos);
    return view;
  }

  @GetMapping("/associarPagamento/{idAssinatura}/{idPagamento}")
  public ModelAndView associarPagamento(@PathVariable("idAssinatura") Integer idAssinatura,
      @PathVariable("idPagamento") Long idPagamento) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");

    assinaturaService.associarPagamento(idAssinatura, idPagamento);

    ModelAndView view = new ModelAndView("redirect:/ativas");
    return view;
  }

  @GetMapping("/ativar/{conta}/{pagamento}")
  public ModelAndView ativar(@PathVariable("conta") String idConta,
      @PathVariable("pagamento") Long idPagamento) {
    System.out.println("Ativando Conta: " + idConta + " com o pagamento: " + idPagamento);
    if (UsuarioUtil.isLogged(request)) {
      Assinatura assinatura = assinaturaService.ativar(idConta, idPagamento);
      return new ModelAndView("redirect:/inativas");
    } else {
      return new ModelAndView("redirect:/login");
    }
  }

  @GetMapping("/ativarComPendencia/{conta}")
  public ModelAndView ativarComPendencia(@PathVariable("conta") String idConta) {
    if (UsuarioUtil.isLogged(request)) {
      Assinatura assinatura = assinaturaService.ativarComPendencia(idConta);
      return new ModelAndView("redirect:/inativas");
    } else {
      return new ModelAndView("redirect:/login");
    }
  }

  @GetMapping("/todas")
  public ModelAndView todas() {
    if (UsuarioUtil.isLogged(request)) {
      List<Assinatura> assinaturasAtivas = assinaturaService.getList();
      ModelAndView view = new ModelAndView("assinatura/todas");
      view.addObject("assinaturasList", assinaturasAtivas);
      return view;
    } else {
      return new ModelAndView("redirect:/login");
    }
  }



  /**
   * Realiza a alteração de email na assinatura.
   * 
   * @param alterarEmailRequest Request.
   */
  @PostMapping("/alterar-email")
  public void alterarEmail(@RequestBody AlterarEmailRequest alterarEmailRequest) {
    if (UsuarioUtil.isLogged(request)) {
      try {
        System.out.println(new ObjectMapper().writeValueAsString(alterarEmailRequest));
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      assinaturaService.alterarEmail(alterarEmailRequest);
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }

  /**
   * Realiza a alteração de vencimento na assinatura.
   * 
   * @param alterarVencimentoRequest Request.
   */
  @PostMapping("/alterar-vencimento")
  public void alterarVencimento(@RequestBody AlterarVencimentoRequest alterarVencimentoRequest) {
    if (UsuarioUtil.isLogged(request)) {
      assinaturaService.alterarVencimento(alterarVencimentoRequest);
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }

  @PostMapping("/excluir-pagamento")
  public void excluirPagamento(
      @RequestBody ExcluirAssinaturaPagamentoRequest excluirPagamentoRequest) {
    if (UsuarioUtil.isLogged(request)) {
      try {
        System.out.println(new ObjectMapper().writeValueAsString(excluirPagamentoRequest));
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      assinaturaService.excluirPagamento(excluirPagamentoRequest);
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }


  /**
   * Retorna a assinatura com o ID indicado.
   * 
   * @param idAssinatura ID da assinatura.
   * @return Assinatura.
   */
  @GetMapping("/get/{idAssinatura}")
  public AssinaturaResponse get(@PathVariable(name = "idAssinatura") Integer idAssinatura) {
    if (UsuarioUtil.isLogged(request)) {
      Assinatura assinatura = assinaturaService.get(idAssinatura);
      if (Objects.isNull(assinatura)) {
        throw new BusinessException("Assinatura não encontrada.");
      }
      return AssinaturaResponseConverter.convert(assinatura);
    } else {
      throw new BusinessException("Usuário não está logado.");
    }
  }

  @GetMapping("/list")
  public List<AssinaturaView> list() {
    if (UsuarioUtil.isLogged(request)) {
      return assinaturaService.getListView();
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }

  /**
   * Retorna o pagamento de assinatura com o ID informado.
   * 
   * @param idPagamento ID do pagamento na assinatura.
   * @return Pagamento da assinatura.
   */
  @GetMapping("/get-pagamento/{idPagamento}")
  public AssinaturaPagamentoResponse getPagamento(
      @PathVariable(name = "idPagamento") Integer idPagamento) {
    if (UsuarioUtil.isLogged(request)) {
      AssinaturaPagamento assinaturaPagamento = assinaturaService.getPagamento(idPagamento);
      if (Objects.isNull(assinaturaPagamento)) {
        throw new BusinessException("Assinatura não encontrada.");
      }
      return AssinaturaPagamentoResponseConverter.convert(assinaturaPagamento);
    } else {
      throw new BusinessException("Usuário não está logado.");
    }
  }

  /**
   * Realiza a pausa de execução de ordens da assinatura.
   * 
   * @param conta ID da conta.
   * @return
   */
  @GetMapping("/pausar/{conta}")
  public String pausar(@PathVariable("conta") String conta) {
    if (UsuarioUtil.isLogged(request)) {
      List<Assinatura> assinaturas = assinaturaService.getList(conta);
      for (Assinatura assinatura : assinaturas) {
        assinatura.setPausado(true);
        assinaturaService.gravar(assinatura);
        routerService.pause(conta);
      }
      return "ok";
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }

  }

  /**
   * Retorma a execução de ordens da assinatura.
   * 
   * @param idConta ID da conta.
   * @return
   */
  @GetMapping("/retomar/{conta}")
  public String retomar(@PathVariable("conta") String idConta) {
    if (UsuarioUtil.isLogged(request)) {
      List<Assinatura> assinaturas = assinaturaService.getList(idConta);
      for (Assinatura assinatura : assinaturas) {
        assinatura.setPausado(false);
        assinaturaService.gravar(assinatura);
        routerService.resume(idConta);
      }
      return "ok";
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }


  /**
   * Realiza a exclusão do expert da assinatura com ID indicado.
   * 
   * @param idAssinaturaExpert ID do expert na assinatura.
   * @return
   */
  @GetMapping("/excluir-expert/{idAssinaturaExpert}")
  public String excluirAutomacao(@PathVariable("idAssinaturaExpert") Integer idAssinaturaExpert) {
    if (UsuarioUtil.isLogged(request)) {
      assinaturaService.excluirExpert(idAssinaturaExpert);
      return "ok";
    } else {
      throw new RuntimeException("Usuário não está logado.");
    }
  }
}
