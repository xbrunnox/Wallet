package br.app.grid.wallet.util.converter;

import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.web.request.RegistrarCandleRequest;
import br.app.grid.wallet.web.response.AtivoCandlesResponse;
import br.app.grid.wallet.web.response.CandleResponse;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
public class CandleConverter {
  
  public static Candle convert(AtivoCandlesResponse response, CandleResponse candleResponse) {
    return Candle.builder()
        .abertura(candleResponse.getOpen())
        .ativo(response.getAtivo())
        .dataHora(candleResponse.getDateTime())
        .fechamento(candleResponse.getClose())
        .maxima(candleResponse.getHigh())
        .minima(candleResponse.getLow())
        .spread(candleResponse.getSpread())
        .tickVolume(candleResponse.getTickVolume())
        .timeFrame(response.getTimeFrame())
        .volume(candleResponse.getVolume())
        .build();
  }

  public static Candle convert(RegistrarCandleRequest request) {
    return Candle.builder()
        .abertura(request.getOpen())
        .ativo(request.getAtivo())
        .dataHora(request.getDateTime())
        .fechamento(request.getClose())
        .maxima(request.getHigh())
        .minima(request.getLow())
        .spread(request.getSpread())
        .tickVolume(request.getTickVolume())
        .timeFrame(request.getTimeFrame())
        .volume(request.getVolume())
        .build();
  }

}
