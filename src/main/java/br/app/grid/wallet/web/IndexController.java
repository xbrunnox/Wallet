package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.dividendo.Dividendo;
import br.app.grid.wallet.dividendo.DividendoService;
import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.grafico.SerieInt;
import br.app.grid.wallet.homebroker.HomeBroker;
import br.app.grid.wallet.homebroker.HomeBrokerService;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.log.LogComando;
import br.app.grid.wallet.log.LogComandoService;
import br.app.grid.wallet.log.LogOnline;
import br.app.grid.wallet.log.LogOnlineService;
import br.app.grid.wallet.meta.ContaPosicoesMT;
import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.meta.PosicaoMT;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.resultado.ResultadoService;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.FiiUtil;
import br.app.grid.wallet.util.converter.ContaResultadoAssinaturaResponseConverter;
import br.app.grid.wallet.web.response.ContaResultadoAssinaturaResponse;
import br.app.grid.wallet.web.response.PosicaoGroupResponse;
import br.app.grid.wallet.web.response.PosicaoResponse;

@RestController
@RequestMapping("")
public class IndexController {

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private DividendoService dividendoService;

  @Autowired
  private ContaService contaService;

  @Autowired
  private RouterService routerService;

  @Autowired
  private LogComandoService logComandoService;

  @Autowired
  private LogOnlineService logOnlineService;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private TradeService tradeService;

  @Autowired
  private RoboService automacaoService;

  @Autowired
  private HomeBrokerService homeBrokerService;

  @Autowired
  private ResultadoService resultadoService;

  @GetMapping("/acompanhamento/{expert}")
  public @ResponseBody ModelAndView acompanhamento(@PathVariable("expert") String expert) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    ModelAndView modelAndView = new ModelAndView("acompanhamento/expert");
    modelAndView.addObject("expert", expert);
    return modelAndView;
  }

  @GetMapping("/dividendo/{ativo}")
  public @ResponseBody FiiDividendo ultimo(@PathVariable("ativo") String ativo) {
    Dividendo dividendo = dividendoService.getDividendo(ativo);
    if (dividendo == null)
      return null;
    return new FiiDividendo(dividendo);
  }

  @GetMapping("/dividendoDebug/{ativo}")
  public @ResponseBody FiiDividendo ultimoDebug(@PathVariable("ativo") String ativo) {
    List<FiiDividendo> historico = FiiUtil.getHistorico(ativo.toLowerCase());
    if (historico == null || historico.size() == 0)
      return null;
    return historico.get(0);
  }

  @GetMapping("/dividendos/{ativo}")
  public @ResponseBody List<FiiDividendo> historico(@PathVariable("ativo") String ativo) {
    return FiiUtil.getHistorico(ativo.toLowerCase());
  }

  @GetMapping("/delta")
  public ModelAndView delta() {
    ModelAndView view = new ModelAndView("index/delta");
    return view;
  }

  @GetMapping("/contas")
  public ModelAndView contas() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<ContaResultadoAssinaturaResponse> contas =
        ContaResultadoAssinaturaResponseConverter.converter(contaService.getListContaResultado());
    List<Assinatura> assinaturas = assinaturaService.getList();
    Map<String, LocalDate> mapaVencimentos = new HashMap<>();
    for (Assinatura assinatura : assinaturas) {
      LocalDate data = mapaVencimentos.get(assinatura.getConta().getId());
      if (data == null)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
      else if (data.compareTo(assinatura.getDataVencimento()) < 0)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
    }
    Double total = 0.0;
    for (ContaResultadoAssinaturaResponse conta : contas) {
      if (conta.getResultado() != null)
        total += conta.getResultado().doubleValue();
      conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
    }
    ModelAndView view = new ModelAndView("index/contas");
    view.addObject("contasList", contas);
    view.addObject("contas", contas.size());
    view.addObject("total", total);
    return view;
  }

  @GetMapping("/resultados")
  public ModelAndView resultados() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<ContaResultadoAssinaturaResponse> contas =
        ContaResultadoAssinaturaResponseConverter.converter(contaService.getListContaResultado());
    List<Assinatura> assinaturas = assinaturaService.getList();
    Map<String, LocalDate> mapaVencimentos = new HashMap<>();
    for (Assinatura assinatura : assinaturas) {
      LocalDate data = mapaVencimentos.get(assinatura.getConta().getId());
      if (data == null)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
      else if (data.compareTo(assinatura.getDataVencimento()) < 0)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
    }
    Double total = 0.0;
    for (int i = contas.size() - 1; i >= 0; i--) {
      ContaResultadoAssinaturaResponse conta = contas.get(i);
      if (conta.getResultado() == null || conta.getResultado().compareTo(BigDecimal.ZERO) == 0) {
        contas.remove(i);
      } else {
        total += conta.getResultado().doubleValue();
        conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
      }
    }
    ModelAndView view = new ModelAndView("index/resultados");
    view.addObject("contasList", contas);
    view.addObject("contas", contas.size());
    view.addObject("total", total);
    return view;
  }

  @GetMapping("/resultados/{expert}")
  public ModelAndView resultados(@PathVariable(name = "expert") String expert) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<ContaResultadoAssinaturaResponse> contas = resultadoService.getResultado(expert);
    List<Assinatura> assinaturas = assinaturaService.getList();
    Map<String, LocalDate> mapaVencimentos = new HashMap<>();
    for (Assinatura assinatura : assinaturas) {
      LocalDate data = mapaVencimentos.get(assinatura.getConta().getId());
      if (data == null)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
      else if (data.compareTo(assinatura.getDataVencimento()) < 0)
        mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
    }
    Double total = 0.0;
    for (int i = contas.size() - 1; i >= 0; i--) {
      ContaResultadoAssinaturaResponse conta = contas.get(i);
      if (conta.getResultado() == null || conta.getResultado().compareTo(BigDecimal.ZERO) == 0) {
        contas.remove(i);
      } else {
        total += conta.getResultado().doubleValue();
        conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
      }
    }
    ModelAndView view = new ModelAndView("index/resultados");
    view.addObject("automacao", expert);
    view.addObject("contasList", contas);
    view.addObject("contas", contas.size());
    view.addObject("total", total);
    return view;
  }

  @GetMapping("/pendencias")
  public ModelAndView pendencias() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<AssinaturaPendenciaView> pendencias = assinaturaService.getPendencias();
    ModelAndView view = new ModelAndView("assinatura/pendencias");
    view.addObject("pendenciasList", pendencias);
    return view;
  }

  /**
   * Exibe as assinatura ativas.
   * 
   * @return
   */
  @GetMapping("/ativas")
  public ModelAndView ativas() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<AssinaturaAtivaView> assinaturasAtivas = assinaturaService.getAtivas();
    ModelAndView view = new ModelAndView("assinatura/ativas");
    view.addObject("ativasList", assinaturasAtivas);
    return view;
  }

  @GetMapping
  public ModelAndView index() {
    if (!UsuarioUtil.isLogged(request)) {
      return new ModelAndView("redirect:/login");
    }
    ModelAndView view = new ModelAndView("index/index");
    return view;
  }

  @GetMapping("/inativas")
  public ModelAndView inativas() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");

    List<Conta> contasInativas = assinaturaService.getContasInativas();

    ModelAndView view = new ModelAndView("index/inativas");
    view.addObject("contasList", contasInativas);
    view.addObject("contas", contasInativas.size());
    return view;
  }

  @GetMapping("/registrar")
  public ModelAndView registrar() {
    ModelAndView view = new ModelAndView("index/registrar");
    return view;
  }

  @GetMapping("/login")
  public ModelAndView login() {
    ModelAndView view = new ModelAndView("index/login");
    return view;
  }

  @GetMapping("/status")
  public ModelAndView status() {
    ModelAndView view = new ModelAndView("endpoint/status");
    EndpointStatusResponse status = routerService.getStatus();
    Collections.sort(status.getOnlineUsers(), new Comparator<ClienteUser>() {

      @Override
      public int compare(ClienteUser o1, ClienteUser o2) {
        if (o1.getPausado() && !o2.getPausado())
          return -1;
        if (!o1.getPausado() && o2.getPausado())
          return 1;
        return StringUtils.stripAccents(o1.getNome())
            .compareTo(StringUtils.stripAccents(o2.getNome()));
      }

    });
    view.addObject("status", status);
    return view;
  }

  @GetMapping("/posicoes")
  public ModelAndView posicoes() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<PosicaoResponse> posicoes = new ArrayList<>();
    Map<String, PosicaoGroupResponse> mapaPosicaoGrupo = new HashMap<>();

    EndpointPositions endpointPosicoes = routerService.getPosicoes();
    if (endpointPosicoes != null && endpointPosicoes.getContas() != null) {
      for (ContaPosicoesMT conta : endpointPosicoes.getContas()) {
        if (conta != null && conta.getPosicoes() != null) {
          for (PosicaoMT posicao : conta.getPosicoes()) {
            if (posicao != null) {
              String posicaoTitulo =
                  posicao.getAtivo() + " - " + posicao.getVolume() + posicao.getDirecao();
              PosicaoGroupResponse grupo = mapaPosicaoGrupo.get(posicaoTitulo);
              if (grupo == null) {
                grupo = PosicaoGroupResponse.builder().ativo(posicao.getAtivo())
                    .direcao(posicao.getDirecao()).posicoes(new ArrayList<>()).titulo(posicaoTitulo)
                    .volume(posicao.getVolume()).build();
                mapaPosicaoGrupo.put(posicaoTitulo, grupo);
              }
              PosicaoResponse posicaoResponse = PosicaoResponse.builder()
                  .abertura(posicao.getAbertura()).ativo(posicao.getAtivo()).conta(conta.getConta())
                  .data(posicao.getData()).direcao(posicao.getDirecao()).expert(posicao.getExpert())
                  .nome(conta.getNome()).profit(posicao.getProfit()).volume(posicao.getVolume())
                  .servidor(conta.getServer()).corretora(conta.getCorretora()).build();
              posicoes.add(posicaoResponse);
              grupo.add(posicaoResponse);
            }
          }
        }
      }
      Collections.sort(posicoes, new Comparator<PosicaoResponse>() {
        @Override
        public int compare(PosicaoResponse o1, PosicaoResponse o2) {
          int resultado = o1.getAtivo().compareTo(o2.getAtivo());
          if (resultado != 0)
            return resultado;
          resultado = o1.getDirecao().compareTo(o2.getDirecao());
          if (resultado != 0)
            return resultado;
          resultado = o1.getVolume().compareTo(o2.getVolume());
          if (resultado != 0)
            return resultado;
          return 0;
        }
      });
    }

    List<PosicaoGroupResponse> retornoPosicoesGrupo = new ArrayList<>();
    for (String titulo : mapaPosicaoGrupo.keySet()) {
      retornoPosicoesGrupo.add(mapaPosicaoGrupo.get(titulo));
    }

    Collections.sort(retornoPosicoesGrupo, new Comparator<PosicaoGroupResponse>() {

      @Override
      public int compare(PosicaoGroupResponse o1, PosicaoGroupResponse o2) {
        int resultado = o1.getPosicoes().size() - o2.getPosicoes().size();
        if (resultado != 0)
          return resultado;
        return o1.getAtivo().compareTo(o2.getAtivo());
      }

    });

    ModelAndView modelAndView = new ModelAndView("index/posicoes");
    modelAndView.addObject("posicoes", posicoes);
    modelAndView.addObject("posicoesGrupos", retornoPosicoesGrupo);
    return modelAndView;
  }

  @GetMapping("/migrar")
  public ModelAndView migrar() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<Conta> contas = contaService.getList();

    ModelAndView modelAndView = new ModelAndView("index/migrar");
    modelAndView.addObject("contasList", contas);
    return modelAndView;
  }

  @GetMapping("/migrar/{contaOrigem}/{contaDestino}")
  public void migrar(@PathVariable("contaOrigem") String contaOrigem,
      @PathVariable("contaDestino") String contaDestino) {
    assinaturaService.migrar(contaOrigem, contaDestino);
  }

  @GetMapping("/monitor")
  public ModelAndView monitor() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    List<LogOnline> logsOnline = logOnlineService.getList24Horas();
    List<SerieInt> series = new ArrayList<>();
    SerieInt usuariosOnline = new SerieInt();
    SerieInt expertsOnline = new SerieInt();
    usuariosOnline.setName("Usuários");
    expertsOnline.setName("Experts");
    for (LogOnline log : logsOnline) {
      usuariosOnline.addData(log.getUsuarios());
      usuariosOnline.addCategoria(formatter.format(log.getHorario()));
      expertsOnline.addData(log.getExperts());
      expertsOnline.addCategoria(formatter.format(log.getHorario()));
    }
    series.add(usuariosOnline);
    series.add(expertsOnline);
    ModelAndView modelAndView = new ModelAndView("index/monitor");
    modelAndView.addObject("chartData", series);
    return modelAndView;
  }

  @GetMapping("/logs")
  public ModelAndView logs() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<LogComando> logs = logComandoService.getListDia();
    ModelAndView modelAndView = new ModelAndView("log/logs");
    modelAndView.addObject("logsList", logs);
    return modelAndView;

  }

  @GetMapping("/detalhes/{conta}")
  public ModelAndView detalhes(@PathVariable(name = "conta") String idConta) {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    BigDecimal total = BigDecimal.ZERO;
    List<Trade> trades = tradeService.getList(idConta);
    for (Trade trade : trades) {
      total = total.add(BigDecimal.valueOf(trade.getResultado()));
    }
    Conta conta = contaService.get(idConta);

    List<Assinatura> assinaturas = assinaturaService.getList(idConta);
    List<Assinatura> subcontas = new ArrayList<>();
    Assinatura assinatura = null;
    if (assinaturas.size() > 0) {
      assinatura = assinaturas.get(0);
      subcontas = assinaturaService.getListSubContas(assinatura.getId());
    }

    List<AssinaturaPagamento> pagamentos = assinaturaService.getListPagamentos(idConta);
    List<AssinaturaExpert> experts = assinaturaService.getExperts(assinatura);

    ModelAndView view = new ModelAndView("conta/detalhes");
    view.addObject("assinatura", assinatura);
    view.addObject("subContaList", subcontas);
    view.addObject("tradesList", trades);
    view.addObject("pagamentosList", pagamentos);
    view.addObject("expertList", experts);
    view.addObject("total", total);
    view.addObject("conta", conta);
    return view;

  }

  @GetMapping("/terminal")
  public ModelAndView terminal() {
    ModelAndView view = new ModelAndView("index/terminal");
    return view;
  }

  @GetMapping("/assinaturas")
  public ModelAndView assinaturas() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    ModelAndView view = new ModelAndView("menu/assinaturas");
    return view;
  }

  @GetMapping("/pagamento-tratamento-pendente")
  public ModelAndView pagamentoTratamentoPendente() {
    if (!UsuarioUtil.isLogged(request)) {
      return new ModelAndView("redirect:/login");
    }
    List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();
    Collections.sort(pagamentos, new Comparator<Pagamento>() {
      @Override
      public int compare(Pagamento p1, Pagamento p2) {
        return p2.getDataAtualizacao().compareTo(p1.getDataAtualizacao());
      }
    });
    ModelAndView view = new ModelAndView("pagamento/tratamento-pendente");
    view.addObject("pagamentosList", pagamentos);
    return view;
  }

  /**
   * Realiza a exibição do Home Broker.
   * 
   * @return View do Home Broker.
   */
  @GetMapping("/home-broker")
  public ModelAndView homebroker() {
    if (!UsuarioUtil.isLogged(request))
      return new ModelAndView("redirect:/login");
    List<Robo> automacoes = automacaoService.getListEnabled();
    List<HomeBroker> homeBrokers = homeBrokerService.getList();
    ModelAndView view = new ModelAndView("index/home-broker");
    view.addObject("automacaoList", automacoes);
    view.addObject("homeBrokerList", homeBrokers);
    return view;
  }
}
