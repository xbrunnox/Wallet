package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;

@Controller
@RequestMapping("/trade")
public class TradeController {

	@Autowired
	private TradeService tradeService;

	@PostMapping("/registrar")
	public @ResponseBody String ping(String ativo, Double volume, Double lucro, Double entrada, Double saida,
			String direcao) {
		Trade trade = Trade.builder().ativo(ativo).volume(volume).lucro(lucro).entrada(entrada).saida(saida)
				.direcao(direcao).build();
		tradeService.gravar(trade);
		return "ok";
	}

}
