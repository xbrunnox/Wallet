package br.app.grid.wallet.backtest.vo;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacktestOperacaoResultadoDiaVOId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private LocalDate data;

}
