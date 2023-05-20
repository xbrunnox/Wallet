package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "backtest_operacao_resultado_dia_view")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BacktestOperacaoResultadoDiaVOId.class)
public class BacktestOperacaoResultadoDiaVO {

  @Id
  private Integer id;

  @Id
  private LocalDate data;

  private BigDecimal resultado;

  private Integer operacoes;

}
