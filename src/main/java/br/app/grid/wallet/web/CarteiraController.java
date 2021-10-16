package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.historico.Historico;
import br.app.grid.wallet.historico.HistoricoService;
import br.app.grid.wallet.resumo.Resumo;
import br.app.grid.wallet.resumo.ResumoService;

@RestController
@RequestMapping("/carteira")
public class CarteiraController {

	@Autowired
	private ResumoService resumoService;
	
	@Autowired
	private HistoricoService historicoService;

	@GetMapping("/resumo/{idCarteira}")
	public @ResponseBody Resumo resumo(@PathVariable("idCarteira") Integer idCarteira) {
		return resumoService.getResumo(idCarteira);
	}
	
	@GetMapping("/historico/{idCarteira}")
	public @ResponseBody Historico historico(@PathVariable("idCarteira") int idCarteira) {
		return historicoService.get(idCarteira);
	}
}
