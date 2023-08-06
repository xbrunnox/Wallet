package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.dashboard.DashboardDelta;
import br.app.grid.wallet.indicador.IndicadorService;
import br.app.grid.wallet.indicador.Rsi;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 23 de maio de 2023.
 */
@RestController
@RequestMapping("/indicador")
public class IndicadorController {

	@Autowired
	private IndicadorService indicadorService;

	@GetMapping("/delta")
	private DashboardDelta delta() {
		return indicadorService.getDelta();
	}

	@GetMapping("/rsi/{ativo}")
	private Rsi rsi(@PathVariable(name = "ativo") String ativo) {
		return indicadorService.getRsi(ativo);
	}

}
