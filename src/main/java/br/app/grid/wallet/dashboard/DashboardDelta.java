package br.app.grid.wallet.dashboard;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDelta {
  
  private BigDecimal amplitudeMaxima;
  private BigDecimal amplitudeMedia;
  
  private BigDecimal deltaInferior;
  private BigDecimal deltaSuperior;
  private BigDecimal delta;
  
  private BigDecimal vwap;
  
  private BigDecimal preco;
  
  private BigDecimal inicioVendaDelta;
  private BigDecimal vendaDelta;
  private BigDecimal fimVendaDelta;
  
  private BigDecimal inicioCompraDelta;
  private BigDecimal compraDelta;
  private BigDecimal fimCompraDelta;
  
  private List<DashboardDeltaDia> deltas;

}
