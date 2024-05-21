package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 09 de maio de 2023.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BacktestDiaVO {

  private LocalDate data;

  private BigDecimal resultado;

  private BigDecimal acumulado;

  private List<BacktestOperacaoVO> operacoes;

  private int acertos;

  private int erros;

  private BigDecimal maiorGanho;

  private BigDecimal maiorStop;

  public void add(BacktestOperacaoVO operacao) {
    operacoes.add(operacao);
  }

  public void atualizarEstatisticas() {
    maiorGanho = BigDecimal.ZERO;
    maiorStop = BigDecimal.ZERO;
    for (BacktestOperacaoVO operacao : operacoes) {
      if (operacao.getLucro().compareTo(BigDecimal.ZERO) > 0) {
        acertos++;
        if (operacao.getLucro().compareTo(maiorGanho) > 0)
          maiorGanho = operacao.getLucro();
      } else {
        erros++;
        if (operacao.getLucro().compareTo(maiorStop) < 0)
          maiorStop = operacao.getLucro();
      }
    }
  }
}
