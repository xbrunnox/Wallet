package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaView;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.trade.TradeResumoContaResponse;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.trade.vo.TradeVO;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.converter.OperacaoResponseConverter;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;
import br.app.grid.wallet.web.response.OperacaoResponse;

/**
 * <b>OperacaoController</b><br>
 * Controlador WEB responsável pelas operações relativas a classe Operacao.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@RestController
@RequestMapping("/operacao")
public class OperacaoController {

  @Autowired
  private OperacaoService service;

  @Autowired
  private TradeService tradeService;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private RouterService routerService;

  @PostMapping("/registrar")
  public Operacao registrar(@RequestBody GravarOperacaoRequest request) {
    return service.save(request);
  }

  @GetMapping("/listar")
  public List<OperacaoResponse> listar() {
    return OperacaoResponseConverter.converter(service.getList());
  }

  @GetMapping("/{id}")
  public OperacaoResponse get(@PathVariable(name = "id") int id) {
    return OperacaoResponseConverter.converter(service.get(id));
  }

  @GetMapping("/andamento")
  public ModelAndView emAndamento() {
    long inicio = System.currentTimeMillis();
    if (!UsuarioUtil.isLogged(request)) {
      return new ModelAndView("redirect:/login");
    }
    Map<String, String> mapaStatus = new HashMap<>();
    Map<String, AssinaturaView> mapaAssinaturas = new HashMap<>();

    List<AssinaturaView> assinaturas = assinaturaService.getListView();
    for (AssinaturaView assinatura : assinaturas) {
      mapaAssinaturas.put(assinatura.getConta(), assinatura);
    }

    EndpointStatusResponse status = routerService.getStatus(request);
    for (ClienteUser user : status.getOnlineUsers()) {
      mapaStatus.put(user.getLicenca(), user.getLicenca());
    }

    // List<Operacao> operacoes = service.getList();
    List<Operacao> operacoes = service.getList(UsuarioUtil.getAfiliado(request));
    List<OperacaoResponse> operacoesResponse = OperacaoResponseConverter.converter(operacoes);
    for (OperacaoResponse operacao : operacoesResponse) {
      operacao.setOnline(mapaStatus.containsKey(operacao.getConta()));
      if (mapaAssinaturas.get(operacao.getConta()) != null) {
        operacao.setMaquina(mapaAssinaturas.get(operacao.getConta()).getMaquina());
        operacao.setCorretora(mapaAssinaturas.get(operacao.getConta()).getCorretora());
        operacao.setServidor(mapaAssinaturas.get(operacao.getConta()).getServidor());
      }
    }

    Collections.sort(operacoesResponse, new Comparator<OperacaoResponse>() {

      @Override
      public int compare(OperacaoResponse o1, OperacaoResponse o2) {
        if (o1.getOnline() && !o2.getOnline()) {
          return 1;
        }
        if (!o1.getOnline() && o2.getOnline()) {
          return -1;
        }
        int resultado = o2.getData().compareTo(o1.getData());
        if (resultado != 0) {
          return resultado;
        }
        return o1.getConta().compareTo(o2.getConta());
      }

    });

    LocalDate data = LocalDate.now();
    // List<Trade> trades = tradeService.getList(data);
    long inicioTradeVO = System.currentTimeMillis();
    List<TradeVO> tradesVO = tradeService.getListVO(UsuarioUtil.getAfiliado(request), data);
    System.out
        .println("Carregar trades VO " + (System.currentTimeMillis() - inicioTradeVO) + " ms");
    Map<String, TradeResumoContaResponse> mapaContaResponse = new HashMap<>();
    List<TradeResumoContaResponse> resumos = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    for (TradeVO trade : tradesVO) {
      total = total.add(trade.getResultado());
      TradeResumoContaResponse resumo = mapaContaResponse.get(trade.getConta());
      if (resumo == null) {
        resumo = TradeResumoContaResponse.builder().conta(trade.getConta())
            .corretora(trade.getCorretora()).nome(trade.getNome()).operacoes(0)
            .total(BigDecimal.ZERO).maquina(mapaAssinaturas.get(trade.getConta()).getMaquina())
            .build();
        mapaContaResponse.put(trade.getConta(), resumo);
        resumos.add(resumo);
      }
      resumo.setOperacoes(resumo.getOperacoes() + 1);
      resumo.setTotal(resumo.getTotal().add(trade.getResultado()));
    }

    Collections.sort(resumos, new Comparator<TradeResumoContaResponse>() {
      @Override
      public int compare(TradeResumoContaResponse o1, TradeResumoContaResponse o2) {
        int resultado = o2.getTotal().compareTo(o1.getTotal());
        if (resultado != 0)
          return resultado;
        return o1.getNome().compareTo(o2.getNome());
      }
    });
    System.out.println("Tempo total: " + (System.currentTimeMillis() - inicio) + " ms");
    ModelAndView view = new ModelAndView("operacao/andamento");
    view.addObject("operacoesList", operacoesResponse);
    view.addObject("tradesList", tradesVO);
    view.addObject("total", total);
    view.addObject("resumosList", resumos);
    view.addObject("data", data);
    view.addObject("afiliado", UsuarioUtil.getAfiliado(request));
    return view;
  }

  @GetMapping("/acompanhamento/{expert}")
  public void acompanhamento(@PathVariable(name = "id") String expert) {
    // return Endpoint.getInstance();
  }

}
