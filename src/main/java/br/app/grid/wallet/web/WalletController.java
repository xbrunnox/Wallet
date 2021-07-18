package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.cotacao.service.CotacaoService;
import br.app.grid.wallet.resumo.Resumo;
import br.app.grid.wallet.resumo.ResumoPosicao;
import br.app.grid.wallet.resumo.ResumoService;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de julho de 2021.
 *
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private CotacaoService cotacaoService;

	@Autowired
	private ResumoService resumoService;

	@GetMapping("/atualizar")
	public @ResponseBody String atualizar() {
		cotacaoService.atualizar();
		return "ok";
	}

	@GetMapping("/resumo/{idCarteira}")
	public @ResponseBody String getResumo(@PathVariable("idCarteira") int idCarteira) {
		Resumo resumo = resumoService.getResumo(idCarteira);
		String html = "";
		html += "<table width=\"100%\">";
		html += "<tr>";
		html += "<td>";
		html += "Ativo";
		html += "</td>";
		html += "<td>";
		html += "Qtd.";
		html += "</td>";
		html += "<td>";
		html += "Valor";
		html += "</td>";
		html += "<td>";
		html += "Strike";
		html += "</td>";
		html += "<td>";
		html += "Total";
		html += "</td>";
		html += "</tr>";
		for (ResumoPosicao posicao : resumo.getPosicoes()) {
			html += "<tr>";
			html += "<td>";
			html += posicao.getAtivo();
			html += "</td>";
			html += "<td>";
			html += posicao.getQuantidade();
			html += "</td>";
			html += "<td>";
			html += posicao.getValor();
			html += "</td>";
			html += "<td>";
			html += posicao.getStrike();
			html += "</td>";
			html += "<td>";
			html += posicao.getTotal();
			html += "</td>";
			html += "</tr>";
		}
		html += "</table>";
		html += "<br>";
		html += "<br>";
		html += "Saldo:" + resumo.getSaldo();
		html += "<br>";
		html += "Total:" + resumo.getTotal();

		return html;
	}

}
