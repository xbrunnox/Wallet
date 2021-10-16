package br.app.grid.wallet.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.util.FiiUtil;

@RestController
@RequestMapping("")
public class IndexController {

	@GetMapping("/dividendo/{ativo}")
	public FiiDividendo ultimo(@PathVariable("ativo") String ativo) {
		List<FiiDividendo> historico = FiiUtil.getHistorico(ativo.toLowerCase());
		if (historico == null || historico.size() == 0)
			return null;
		return historico.get(0);
	}
	
	@GetMapping("/dividendoDebug/{ativo}")
	public FiiDividendo ultimoDebug(@PathVariable("ativo") String ativo) {
		List<FiiDividendo> historico = FiiUtil.getHistorico(ativo.toLowerCase());
		if (historico == null || historico.size() == 0)
			return null;
		return historico.get(0);
	}
	
	@GetMapping("/dividendos/{ativo}")
	public List<FiiDividendo> historico(@PathVariable("ativo") String ativo) {
		return FiiUtil.getHistorico(ativo.toLowerCase());
	}
	
}
