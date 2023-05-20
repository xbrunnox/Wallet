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
  
  private List<DashboardDeltaDia> deltas;

}
