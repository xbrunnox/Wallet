package br.app.grid.wallet.backtest.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacktestOperacaoResultadoMesVOId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private Integer ano;
  
  private Integer mes;

}
