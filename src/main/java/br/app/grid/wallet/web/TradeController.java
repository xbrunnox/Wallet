package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.usuario.UsuarioUtil;

@Controller
@RequestMapping("/trade")
public class TradeController {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/{conta}")
	public ModelAndView trades(@PathVariable(name = "conta") String conta) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		BigDecimal total = BigDecimal.ZERO;
		List<Trade> trades = tradeService.getList(conta);
		for (Trade trade : trades) {
			total = total.add(BigDecimal.valueOf(trade.getResultado()));
		}
		ModelAndView view = new ModelAndView("index/trades");
		view.addObject("tradesList", trades);
//		view.addObject("contas", contas.size());
		view.addObject("afiliado", UsuarioUtil.getAfiliado(request));
		view.addObject("total", total);
		return view;
	}

	@PostMapping("/registrar")
	public @ResponseBody String ping(String ativo, Double volume, Double lucro, Double entrada, Double saida,
			String direcao) {
		// TODO ajeitar o ativo
//		Trade trade = Trade.builder().ativo(null).volume(volume).resultado(lucro).entrada(entrada).saida(saida)
//				.direcao(direcao).build();
//		tradeService.gravar(trade);
		return "ok";
	}

}
