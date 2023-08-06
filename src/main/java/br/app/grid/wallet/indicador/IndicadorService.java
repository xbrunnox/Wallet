package br.app.grid.wallet.indicador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.dashboard.DashboardDelta;
import br.app.grid.wallet.dashboard.DashboardService;
import br.app.grid.wallet.indicador.rsi.RsiService;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 23 de maio de 2023.
 */
@Service
public class IndicadorService {
  
  @Autowired
  private DashboardService dashboardService;
  
  @Autowired
  private RsiService rsiService;

  public DashboardDelta getDelta() {
    DashboardDelta dashDelta = dashboardService.getDashboardDelta();
    return dashDelta;
  }
  
  /**
   * Retorna o RSI do ativo indicado.
   * @param ativo Ativo.
   * @return RSI.
   */
  public Rsi getRsi(String ativo) {
	  return rsiService.getRsi(ativo);
  }

}
