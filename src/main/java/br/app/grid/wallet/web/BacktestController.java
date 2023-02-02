package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.service.BacktestOperacaoService;
import br.app.grid.wallet.backtest.service.BacktestService;
import br.app.grid.wallet.backtest.vo.BacktestMesAnoVO;
import br.app.grid.wallet.web.request.BacktestImportarRequest;

@Controller
@RequestMapping("/backtest")
public class BacktestController {

	@Autowired
	private BacktestService backtestService;
	@Autowired
	private BacktestOperacaoService backtestOperacaoService;
	
	@GetMapping("")
	public ModelAndView get() {
		List<Backtest> backtests = backtestService.getList();
		ModelAndView modelAndView = new ModelAndView("backtest/backtest-selecao");
		modelAndView.addObject("backtestList", backtests);
		return modelAndView;
	}

	@GetMapping("/{idBacktest}/{idBacktest2}/{volume1}/{volume2}")
	public ModelAndView get(@PathVariable("idBacktest") Integer idBacktest,
			@PathVariable("idBacktest2") Integer idBacktest2, @PathVariable("volume1") Integer volume1,
			@PathVariable("volume2") Integer volume2) {
		Backtest backtest = backtestService.get(idBacktest);
		Backtest backtest2 = backtestService.get(idBacktest2);
		List<BacktestMesAnoVO> retorno = backtestOperacaoService.getAcumulado(backtest, BigDecimal.valueOf(volume1));
		List<BacktestMesAnoVO> retorno2 = backtestOperacaoService.getAcumulado(backtest2, BigDecimal.valueOf(volume2));
		System.out.println("Terminou a consulta");
		BigDecimal resultado1 = BigDecimal.ZERO;
		BigDecimal resultado2 = BigDecimal.ZERO;
		for (BacktestMesAnoVO mes : retorno) {
			resultado1 = resultado1.add(mes.getTotal());
		}
		for (BacktestMesAnoVO mes : retorno2) {
			resultado2 = resultado2.add(mes.getTotal());
		}
		ModelAndView modelAndView = new ModelAndView("backtest/backtest");
		modelAndView.addObject("meses1", retorno);
		modelAndView.addObject("meses2", retorno2);
		modelAndView.addObject("resultado1", resultado1);
		modelAndView.addObject("resultado2", resultado2);
		return modelAndView;
	}
	
	@GetMapping("/importar")
	public ModelAndView importar() {
		List<Backtest> backtests = backtestService.getList();
		ModelAndView modelAndView = new ModelAndView("backtest/importar");
		modelAndView.addObject("backtestList", backtests);
		return modelAndView;
	}

	@PostMapping("/importar")
	public void importar(BacktestImportarRequest request) {
		System.out.println(request.getIdBacktest());
		System.out.println(request.getTransacoes());
		backtestService.importar2(request.getIdBacktest(), request.getTransacoes());
	}

}
