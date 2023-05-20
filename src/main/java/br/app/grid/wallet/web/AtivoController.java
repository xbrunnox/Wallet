package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.web.request.CandlesRequest;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@RestController
@RequestMapping("/ativo")
public class AtivoController {
  
  @Autowired
  private CandleService candleService;
  
  /**
   * 
   * @param importarCandlesRequest
   * @return
   */
  @PostMapping("/importar-candles")
  public String importarCandles(@RequestBody CandlesRequest importarCandlesRequest) {
    candleService.importarCandles(importarCandlesRequest);
    return "ok";
  }

}
