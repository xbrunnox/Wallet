package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.cotacao.service.CotacaoService;
import br.app.grid.wallet.indicador.Rsi;
import br.app.grid.wallet.indicador.rsi.RsiService;
import br.app.grid.wallet.util.VersatilUtil;
import br.app.grid.wallet.web.request.CandlesRequest;
import br.app.grid.wallet.web.request.RegistrarCandleRequest;
import br.app.grid.wallet.web.request.RegistrarCotacaoRequest;
import br.app.grid.wallet.web.request.RegistrarRsiRequest;
import br.app.grid.wallet.web.response.CotacaoResponse;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@RestController
@RequestMapping("/ativo")
public class AtivoController {

  @Autowired
  private CandleService candleService;

  @Autowired
  private CotacaoService cotacaoService;
  
  @Autowired
  private RsiService rsiService;

  /**
   * Solicita a importação de Candles.
   * 
   * @param importarCandlesRequest
   * @return
   */
  @PostMapping("/importar-candles")
  public String importarCandles(@RequestBody CandlesRequest importarCandlesRequest) {
    candleService.importarCandles(importarCandlesRequest);
    return "ok";
  }

  /**
   * Realiza o registro de um candle.
   * 
   * @param request Request.
   * @return Candle após a gravação.
   */
  @PostMapping("/registrar-candle")
  public @ResponseBody Candle registrarCandle(@RequestBody RegistrarCandleRequest request) {
    Candle candle = candleService.registrarCandle(request);
    return candle;
  }

  /**
   * Realiza o registro da cotação de um ativo.
   * 
   * @param request Request.
   * @return Cotação.
   */
  @PostMapping("/registrar-cotacao")
  public @ResponseBody CotacaoResponse registrarCotacao(
      @RequestBody RegistrarCotacaoRequest request) {
    System.out.println(VersatilUtil.toJson(request));
    return cotacaoService.registrarCotacao(request);
  }

  /**
   * Retorna a cotação do ativo indicado.
   * 
   * @param ativo Ativo.
   * @return Cotação.
   */
  @GetMapping("/cotacao/{ativo}")
  public CotacaoResponse cotacao(@PathVariable(name = "ativo") String ativo) {
    return cotacaoService.getCotacao(ativo);
  }
  
  /**
   * Realiza o registro do RSI de um ativo.
   * 
   * @param request Request.
   * @return Cotação.
   */
  @PostMapping("/registrar-rsi")
  public @ResponseBody Rsi registrarCotacao(
      @RequestBody RegistrarRsiRequest request) {
    System.out.println(VersatilUtil.toJson(request));
    return rsiService.registrar(request);
  }

}
