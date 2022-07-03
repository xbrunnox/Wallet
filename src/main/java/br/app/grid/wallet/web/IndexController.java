package br.app.grid.wallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.dividendo.Dividendo;
import br.app.grid.wallet.dividendo.DividendoService;
import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.util.FiiUtil;

@RestController
@RequestMapping("")
public class IndexController {

	@Autowired
	private DividendoService dividendoService;

	@GetMapping("/dividendo/{ativo}")
	public @ResponseBody FiiDividendo ultimo(@PathVariable("ativo") String ativo) {
		Dividendo dividendo = dividendoService.getDividendo(ativo);
		if (dividendo == null)
			return null;
		return new FiiDividendo(dividendo);
	}

	@GetMapping("/dividendoDebug/{ativo}")
	public @ResponseBody FiiDividendo ultimoDebug(@PathVariable("ativo") String ativo) {
		List<FiiDividendo> historico = FiiUtil.getHistorico(ativo.toLowerCase());
		if (historico == null || historico.size() == 0)
			return null;
		return historico.get(0);
	}

	@GetMapping("/dividendos/{ativo}")
	public @ResponseBody List<FiiDividendo> historico(@PathVariable("ativo") String ativo) {
		return FiiUtil.getHistorico(ativo.toLowerCase());
	}

	@GetMapping("/delta")
	public ModelAndView delta() {
		ModelAndView view = new ModelAndView("index/delta");
		return view;
	}
}
