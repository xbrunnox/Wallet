package br.app.grid.wallet.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 18 de maio de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDeltaDia {

  private LocalDate data;

  private BigDecimal abertura;
  private BigDecimal fechamento;
  private BigDecimal minima;
  private BigDecimal maxima;
  private BigDecimal amplitude;
  private BigDecimal vwapInferior;
  private BigDecimal vwapSuperior;

  private LocalDateTime dataVwapInferior;
  private LocalDateTime dataVwapSuperior;

}
