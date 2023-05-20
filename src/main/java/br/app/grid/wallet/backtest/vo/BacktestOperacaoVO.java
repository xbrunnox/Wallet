package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 09 de maio de 2023.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "backtest_operacao_view")
public class BacktestOperacaoVO {

  @Id
  private Long id;

  @Column(name = "id_backtest")
  private Integer idBacktest;

  private String direcao;
  
  private LocalDate dataEntrada;
  
  private LocalDate dataSaida;

  private LocalDateTime dataHoraEntrada;

  private LocalDateTime dataHoraSaida;

  private BigDecimal volume;
  private BigDecimal precoEntrada;
  private BigDecimal precoSaida;
  private BigDecimal lucro;

  private Integer duracao;

  @Transient
  private BigDecimal acumulado;

}
