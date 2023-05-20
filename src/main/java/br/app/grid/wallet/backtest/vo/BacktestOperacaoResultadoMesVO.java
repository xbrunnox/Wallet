package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 11 de maio de 2023.
 */
@Entity
@Data
@Table(name = "backtest_operacao_resultado_mes_view")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BacktestOperacaoResultadoMesVOId.class)
public class BacktestOperacaoResultadoMesVO {

  @Id
  private Integer id;

  @Id
  private Integer ano;
  
  @Id
  private Integer mes;

  private BigDecimal resultado;

  private Integer operacoes;

}
