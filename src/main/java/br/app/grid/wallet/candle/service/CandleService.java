package br.app.grid.wallet.candle.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.repository.CandleRepository;
import br.app.grid.wallet.enums.TimeFrameEnum;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.util.VersatilUtil;
import br.app.grid.wallet.util.converter.CandleConverter;
import br.app.grid.wallet.web.request.CandlesRequest;
import br.app.grid.wallet.web.request.RegistrarCandleRequest;
import br.app.grid.wallet.web.response.AtivoCandlesResponse;
import br.app.grid.wallet.web.response.CandleResponse;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@Service
public class CandleService {

  @Autowired
  private CandleRepository candleRepository;

  @Autowired
  private RouterService routerService;

  /**
   * Realiza a gravação do candle no banco de dados.
   * 
   * @param candle Candle.
   * @return Candle após a gravação.
   */
  public Candle gravar(Candle candle) {
    return candleRepository.save(candle);
  }

  public void importarCandles(CandlesRequest importarCandlesRequest) {
    System.out.println("Importando Candles");
    AtivoCandlesResponse response = routerService.getCandles(importarCandlesRequest);
    System.out.println("Candles encontrados: " + response.getCandles().size());
    if (response != null) {
      List<CandleResponse> candles = response.getCandles();
      for (CandleResponse candleResponse : candles) {
        Candle candle = CandleConverter.convert(response, candleResponse);
        try {
          candleRepository.save(candle);
        } catch (Exception e) {
          System.out.println("Falha gravando: " + VersatilUtil.toJson(candle));
        }
      }
    }
  }

  /**
   * Realiza o cálculo do VWAP da data indicada.
   * 
   * @param ativo Ativo.
   * @param timeframe Período
   * @param data Data.
   */
  public void calcularVwap(String ativo, Integer timeframe, LocalDate data) {
    System.out.println("Calculando VWAP " + data);
    List<Candle> candles = candleRepository.getList(ativo, timeframe, VersatilUtil.toDate(data));
    BigDecimal somatorioVolume = BigDecimal.ZERO;
    BigDecimal somatorioPrecoTipico = BigDecimal.ZERO;
    BigDecimal somatorioPrecoTipicoVolume = BigDecimal.ZERO;
    System.out.println("Candles Encontrados: " + candles.size());
    for (Candle candle : candles) {
      somatorioVolume = somatorioVolume.add(BigDecimal.valueOf(candle.getVolume()));

      BigDecimal precoTipico = candle.getAbertura().add(candle.getMaxima()).add(candle.getMinima())
          .add(candle.getFechamento()).divide(BigDecimal.valueOf(4.0), 2, RoundingMode.HALF_UP);


      somatorioPrecoTipicoVolume = somatorioPrecoTipicoVolume
          .add(precoTipico.multiply(BigDecimal.valueOf(candle.getVolume())));


      somatorioPrecoTipico = somatorioPrecoTipico.add(precoTipico);

      BigDecimal vWap = somatorioPrecoTipicoVolume.divide(somatorioVolume, 2, RoundingMode.HALF_UP);
      if (Objects.isNull(candle.getVwap()) || !candle.getVwap().equals(vWap)) {
        candle.setVwap(vWap);
        candleRepository.save(candle);
        System.out.println(VersatilUtil.toJson(candle));
      }
    }
    System.out.println("Calculo finalizado VWAP " + data);
  }

  public List<Candle> getListUltimos(String ativo, TimeFrameEnum timeframe, Integer resultados) {
    return candleRepository.getListUltimos(ativo, timeframe.getValor(), resultados);
  }

  public List<Candle> getList(String ativo, TimeFrameEnum timeframe, LocalDate dataInicial,
      LocalDate dataFinal) {
    return candleRepository.getList(ativo, timeframe.getValor(), VersatilUtil.toDate(dataInicial),
        VersatilUtil.toDate(dataFinal));
  }

  /**
   * Realiza o registro do candle.
   * 
   * @param request Request.
   * @return Candle após o registro.
   */
  public Candle registrarCandle(RegistrarCandleRequest request) {
    Candle candleAnterior =
        candleRepository.get(request.getAtivo(), request.getDateTime(), request.getTimeFrame());

    Candle candle = CandleConverter.convert(request);
    if (candleAnterior != null)
      candle.setId(candleAnterior.getId());

    candleRepository.save(candle);

    calcularVwap(candle.getAtivo(), candle.getTimeFrame(), candle.getDataHora().toLocalDate());

    return candleRepository.get(candle.getId());
  }

  public BigDecimal getUltimoPreco(String ativo) {
    Candle candle = candleRepository.getUltimoPreco(ativo);
    return candle.getFechamento();
  }

  public Candle getUltimoCandle(String ativo, TimeFrameEnum m5) {
    Candle candle = candleRepository.getUltimoCandle(ativo, m5.getValor());
    return candle;
  }
}
