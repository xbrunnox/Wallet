package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BacktestMesAnoVO {

  private int mes;

  private int ano;

  private BigDecimal total;

  private List<BacktestDia> dias;

  private String descricao;

  private int ganhos;
  private int stops;

  private BigDecimal taxaDeAcerto;

  private int diasDeGanho;
  private int diasDePerda;
  
  private BigDecimal maiorGanhoNoDia;
  private BigDecimal maiorPerdaNoDia;

  public void add(LocalDate localDate, BigDecimal lucro) {
    if (dias == null)
      dias = new ArrayList<>();
    for (BacktestDia dia : dias) {
      if (localDate.equals(dia.getData())) {
        if (total == null)
          total = BigDecimal.ZERO;
        total = total.add(lucro);
        dia.add(lucro);
        if (lucro.compareTo(BigDecimal.ZERO) >= 0)
          ganhos++;
        else
          stops++;
        atualizarTaxaDeAcerto();
        return;
      }
    }
    dias.add(BacktestDia.builder().data(localDate).saldo(lucro).build());
    if (total == null)
      total = BigDecimal.ZERO;
    total = total.add(lucro);
    if (lucro.compareTo(BigDecimal.ZERO) >= 0)
      ganhos++;
    else
      stops++;
    atualizarTaxaDeAcerto();
  }

  private void atualizarTaxaDeAcerto() {
    if (ganhos == 0 && stops == 0)
      taxaDeAcerto = BigDecimal.ZERO;
    else
      taxaDeAcerto = BigDecimal.valueOf((ganhos * 100) / (ganhos + stops));
  }
  
  public void atualizarEstatisticas() {
    maiorGanhoNoDia = BigDecimal.ZERO;
    maiorPerdaNoDia = BigDecimal.ZERO;
    diasDeGanho = 0;
    diasDePerda = 0;
    for (BacktestDia dia : dias) {
      if (dia.getSaldo().compareTo(maiorGanhoNoDia) > 0) {
        maiorGanhoNoDia = dia.getSaldo();
      } else if (dia.getSaldo().compareTo(maiorPerdaNoDia) < 0) {
        maiorPerdaNoDia = dia.getSaldo();
      }
      if (dia.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
        diasDePerda++;
      } else {
        diasDeGanho++;
      }
    }
  }

}
