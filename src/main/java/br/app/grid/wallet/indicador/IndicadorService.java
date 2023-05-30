package br.app.grid.wallet.indicador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.dashboard.DashboardDelta;
import br.app.grid.wallet.dashboard.DashboardService;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 23 de maio de 2023.
 */
@Service
public class IndicadorService {
  
  @Autowired
  private CandleService candleService;
  
  @Autowired
  private DashboardService dashboardService;

  public DashboardDelta getDelta() {
    DashboardDelta dashDelta = dashboardService.getDashboardDelta();
    return dashDelta;
  }
  
  

}
