package br.app.grid.wallet.resumo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.carteira.Carteira;
import br.app.grid.wallet.carteira.CarteiraService;
import br.app.grid.wallet.cotacao.Cotacao;
import br.app.grid.wallet.cotacao.service.CotacaoService;
import br.app.grid.wallet.posicao.Posicao;
import br.app.grid.wallet.posicao.PosicaoService;

@Service
public class ResumoService {

	@Autowired
	private CarteiraService carteiraService;
	@Autowired
	private PosicaoService posicaoService;
	@Autowired
	private CotacaoService cotacaoService;

	public Resumo getResumo(int idCarteira) {
		Resumo resumo = new Resumo();
		Carteira carteira = carteiraService.get(idCarteira);
		resumo.setIdCarteira(carteira.getId());
		resumo.setNome(carteira.getNome());
		resumo.setSaldo(carteira.getSaldo());

		List<Posicao> posicoes = posicaoService.getList(carteira);
		for (Posicao posicao : posicoes) {
			Cotacao cotacao = cotacaoService.getUltimaCotacao(posicao.getAtivo());
			resumo.add(posicao.getAtivo().getCodigo(), posicao.getQuantidade(), cotacao.getValor(),
					posicao.getStrike());
		}
		resumo.atualizarTotais();

		return resumo;
	}

}
