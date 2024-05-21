package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
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

import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.ativo.AtivoService;
import br.app.grid.wallet.commons.AccountInfoMeta;
import br.app.grid.wallet.enums.DirecaoOperacaoEnum;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.meta.ContaHistoricoMT;
import br.app.grid.wallet.meta.OperacaoMT;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.VersatilUtil;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;
import br.app.grid.wallet.web.request.MarketOrderRequest;

@RestController
@RequestMapping("/metatrader")
public class MetatraderController {

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private ContaService contaService;

  @Autowired
  private RoboService expertService;

  @Autowired
  private OperacaoService operacaoService;

  @Autowired
  private RouterService routerService;

  @Autowired
  private AtivoService ativoService;

  @Autowired
  private TradeService tradeService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping("/sincronizar/{conta}")
  public String sincronizar(@PathVariable(name = "conta") String conta) {
    System.out.println("Sincronizando");
    ContaHistoricoMT historico =
        routerService.getHistoricoMetatrader(conta, LocalDate.now(), LocalDate.now());
    System.out.println("Historico");
    System.out.println(VersatilUtil.toJson(historico));
    if (historico != null && historico.getOperacoes() != null
        && historico.getOperacoes().size() > 0) {
      List<Operacao> operacoesDoDia = operacaoService.getList(conta, LocalDate.now());
      for (Operacao operacao : operacoesDoDia) {
        operacaoService.excluir(operacao);
      }

      List<Trade> trades = tradeService.getList(conta, LocalDate.now());
      for (Trade trade : trades) {
        tradeService.excluir(trade);
      }

      for (OperacaoMT operacaoMt : historico.getOperacoes()) {
        GravarOperacaoRequest operacao =
            GravarOperacaoRequest.builder().ativo(operacaoMt.getAtivo()).conta(conta)
                .data(operacaoMt.getHorario()).idAfiliado(historico.getIdAfiliado())
                .direcao(operacaoMt.getDirecao() == DirecaoOperacaoEnum.BUY ? "C" : "V")
                .expert(operacaoMt.getExpert()).preco(operacaoMt.getPreco().doubleValue()).tipo("E")
                .volume(operacaoMt.getVolume()).build();
        operacaoService.save(operacao);
      }
    } else if (historico == null) {
      return "O usuário não está online";
    }
    return "ok";
  }

  @GetMapping("/sincronizar/{conta}/{data}")
  public String sincronizar(@PathVariable(name = "conta") String conta,
      @PathVariable(name = "data") String data) {

    LocalDate localDate = LocalDate.parse(data);

    ContaHistoricoMT historico = routerService.getHistoricoMetatrader(conta, localDate, localDate);
    if (historico != null && historico.getOperacoes() != null
        && historico.getOperacoes().size() > 0) {
      try {
        System.out.println(new ObjectMapper().writeValueAsString(historico.getOperacoes()));
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      List<Operacao> operacoesDoDia = operacaoService.getList(conta, localDate);
      for (Operacao operacao : operacoesDoDia) {
        operacaoService.excluir(operacao);
      }

      List<Trade> trades = tradeService.getList(conta, localDate);
      for (Trade trade : trades) {
        tradeService.excluir(trade);
      }

      for (OperacaoMT operacaoMt : historico.getOperacoes()) {
        GravarOperacaoRequest operacao = GravarOperacaoRequest.builder()
            .ativo(operacaoMt.getAtivo()).conta(conta).data(operacaoMt.getHorario())
            .direcao(operacaoMt.getDirecao() == DirecaoOperacaoEnum.BUY ? "C" : "V")
            .idAfiliado(historico.getIdAfiliado()).expert(operacaoMt.getExpert())
            .preco(operacaoMt.getPreco().doubleValue()).tipo("E").volume(operacaoMt.getVolume())
            .build();
        operacaoService.save(operacao);
      }
    } else if (historico == null) {
      return "O usuário não está online";
    }
    return "ok";
  }

  @GetMapping("/account-info")
  public ModelAndView accountInfo() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<AccountInfoMeta> infos = routerService.getAccountInfo();
    ModelAndView view = new ModelAndView("metatrader/account-info");
    view.addObject("infoList", infos);
    view.addObject("afiliado", UsuarioUtil.getAfiliado(request));
    return view;

  }

  @GetMapping("/order/market/{conta}/{ativo}/{volume}/{direcao}")
  public String market(@PathVariable(name = "conta") String conta,
      @PathVariable(name = "ativo") String ativo, @PathVariable(name = "volume") BigDecimal volume,
      @PathVariable(name = "direcao") DirecaoOperacaoEnum direcao) {
    routerService.sendMarketOrder(conta, ativo, volume, direcao);
    return "ok";
  }

  @PostMapping("/order/market")
  public void market(@RequestBody MarketOrderRequest request) {
    if (request.getBilhete() == null)
      request.setBilhete(new Random().nextInt(100000) + 0L);
    if (request.getDataHora() == null)
      request.setDataHora(LocalDateTime.now());
    if (Strings.isEmpty(request.getConta()))
      request.setConta(null);

    try {
      System.out.println(new ObjectMapper().writeValueAsString(request));
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    routerService.sendMarketOrder(request);
  }
}
