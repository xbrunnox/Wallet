package br.app.grid.wallet.candle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Candle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ativo;

  private Integer timeFrame;

  private BigDecimal abertura;
  private BigDecimal fechamento;
  private BigDecimal minima;
  private BigDecimal maxima;
  private BigDecimal spread;
  private BigDecimal vwap;

  private Integer volume;
  private Integer tickVolume;

  private LocalDateTime dataHora;

}
