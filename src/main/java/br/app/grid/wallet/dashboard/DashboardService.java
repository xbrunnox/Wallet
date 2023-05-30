package br.app.grid.wallet.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.cotacao.service.CotacaoService;
import br.app.grid.wallet.enums.TimeFrameEnum;
import br.app.grid.wallet.util.VersatilUtil;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 18 de maio de 2023.
 */
@Service
public class DashboardService {

  @Autowired
  private CandleService candleService;
  
  @Autowired
  private CotacaoService cotacaoService;

  public DashboardDelta getDashboardDelta() {
    Map<LocalDate, DashboardDeltaDia> mapaDatas = new HashMap<>();
    List<DashboardDeltaDia> deltas = new ArrayList<>();

    LocalDateTime dataInicial = null;
    LocalDateTime dataFinal = null;

    List<Candle> candlesDiarios = candleService.getListUltimos("WIN@N", TimeFrameEnum.D1, 15);

    for (Candle candle : candlesDiarios) {
      if (Objects.isNull(dataInicial) || dataInicial.compareTo(candle.getDataHora()) > 0)
        dataInicial = candle.getDataHora();
      if (Objects.isNull(dataFinal) || dataFinal.compareTo(candle.getDataHora()) < 0)
        dataFinal = candle.getDataHora();

      DashboardDeltaDia dashDelta = mapaDatas.get(VersatilUtil.toLocalDate(candle.getDataHora()));
      if (Objects.isNull(dashDelta)) {
        dashDelta = DashboardDeltaDia.builder().abertura(candle.getAbertura())
            .fechamento(candle.getFechamento())
            .amplitude(candle.getMaxima().subtract(candle.getMinima()).abs())
            .data(VersatilUtil.toLocalDate(candle.getDataHora())).maxima(candle.getMaxima())
            .minima(candle.getMinima()).vwapInferior(BigDecimal.ZERO).vwapSuperior(BigDecimal.ZERO)
            .build();
        mapaDatas.put(dashDelta.getData(), dashDelta);
        deltas.add(dashDelta);
      }
    }

    List<Candle> candlesM5 = candleService.getList("WIN@N", TimeFrameEnum.M5,
        VersatilUtil.toLocalDate(dataInicial), VersatilUtil.toLocalDate(dataFinal));
    
//    System.out.println("Data Inicial: "+dataInicial);
//    System.out.println("Data Final: "+dataFinal);

    /*
     * Tratamento dos candles do dia.
     */
    BigDecimal vwap = null;
    BigDecimal preco = candleService.getUltimoPreco("WIN@N");
    for (Candle candle : candlesM5) {
      DashboardDeltaDia dashDelta = mapaDatas.get(VersatilUtil.toLocalDate(candle.getDataHora()));
      // Identificacao do VWAP Inferior
      if (candle.getMinima().compareTo(candle.getVwap()) < 0) {
        BigDecimal diferenca = candle.getVwap().subtract(candle.getMinima()).abs();
//        System.out.println(diferenca);
        if (diferenca.compareTo(dashDelta.getVwapInferior()) > 0) {
          dashDelta.setVwapInferior(diferenca);
          dashDelta.setDataVwapInferior(candle.getDataHora());
        }
      }
      // Identificacao do VWAP Superior
      if (candle.getMaxima().compareTo(candle.getVwap()) > 0) {
        BigDecimal diferenca = candle.getMaxima().subtract(candle.getVwap()).abs();
//        System.out.println(diferenca);
        if (diferenca.compareTo(dashDelta.getVwapSuperior()) > 0) {
          dashDelta.setVwapSuperior(diferenca);
          dashDelta.setDataVwapSuperior(candle.getDataHora());
        }
      }
      if (candle.getVwap() != null)
        vwap = candle.getVwap();
    }
    
    Candle ultimoCandle = candleService.getUltimoCandle("WIN@N", TimeFrameEnum.M5);

    BigDecimal vwapInferior = BigDecimal.ZERO;
    BigDecimal vwapSuperior = BigDecimal.ZERO;
    BigDecimal amplitude = BigDecimal.ZERO;
    BigDecimal amplitudeMaxima = BigDecimal.ZERO;
    BigDecimal delta = BigDecimal.ZERO;
    for (DashboardDeltaDia deltaDia : deltas) {
      vwapInferior = vwapInferior.add(deltaDia.getVwapInferior());
      vwapSuperior = vwapSuperior.add(deltaDia.getVwapSuperior());
      amplitude = amplitude.add(deltaDia.getAmplitude());
      if (deltaDia.getAmplitude().compareTo(amplitudeMaxima) > 0)
        amplitudeMaxima = deltaDia.getAmplitude();
      if (deltaDia.getVwapInferior().compareTo(deltaDia.getVwapSuperior()) > 0) {
        delta = delta.add(deltaDia.getVwapInferior());
      } else {
        delta = delta.add(deltaDia.getVwapSuperior());
      }
    }

    DashboardDelta dashboardDelta = DashboardDelta.builder()
        .delta(delta.divide(BigDecimal.valueOf(deltas.size()), 0, RoundingMode.HALF_UP))
        .deltaInferior(
            vwapInferior.divide(BigDecimal.valueOf(deltas.size()), 0, RoundingMode.HALF_UP))
        .deltaSuperior(
            vwapSuperior.divide(BigDecimal.valueOf(deltas.size()), 0, RoundingMode.HALF_UP))
        .amplitudeMedia(
            amplitude.divide(BigDecimal.valueOf(deltas.size()), 0, RoundingMode.HALF_UP))
        .amplitudeMaxima(amplitudeMaxima).deltas(deltas).build();


    vwap = ultimoCandle.getVwap();

    System.out.println(VersatilUtil.toJson(deltas));
    dashboardDelta.setPreco(cotacaoService.getCotacao("WIN@N").getPreco());
    dashboardDelta.setVwap(ultimoCandle.getVwap());
    dashboardDelta.setVendaDelta(vwap.add(dashboardDelta.getDelta()));
    dashboardDelta.setCompraDelta(vwap.subtract(dashboardDelta.getDelta()));
    // Delta Superior (Venda)
    dashboardDelta
        .setInicioVendaDelta(vwap.add(dashboardDelta.getDelta().multiply(BigDecimal.valueOf(0.8))));
    dashboardDelta
        .setFimVendaDelta(vwap.add(dashboardDelta.getDelta().multiply(BigDecimal.valueOf(1.2))));
    // Delta Inferior (Compra)
    dashboardDelta.setInicioCompraDelta(
        vwap.subtract(dashboardDelta.getDelta().multiply(BigDecimal.valueOf(0.8))));
    dashboardDelta.setFimCompraDelta(
        vwap.subtract(dashboardDelta.getDelta().multiply(BigDecimal.valueOf(1.2))));

    return dashboardDelta;

  }

}
