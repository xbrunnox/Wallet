package br.app.grid.wallet.resultado;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.response.ResultadoGlobalResponse;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.trade.repository.TradeResultadoDiaRepository;
import br.app.grid.wallet.trade.vo.TradeResultadoDiaVO;
import br.app.grid.wallet.web.response.ContaResultadoAssinaturaResponse;

/**
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 28 de fevereiro de 2023.
 */

@Service
public class ResultadoService {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private TradeResultadoDiaRepository tradeResultadoDiaRespository;

	public List<ContaResultadoAssinaturaResponse> getResultado(String expert) {
		Map<String, ContaResultadoAssinaturaResponse> mapa = new HashMap<>();
		List<Trade> trades = tradeService.getListByExpert(expert);

		for (Trade trade : trades) {
			ContaResultadoAssinaturaResponse resultado = mapa.get(trade.getConta().getId());
			if (Objects.isNull(resultado)) {
				resultado = ContaResultadoAssinaturaResponse.builder().corretora(trade.getConta().getCorretora())
						.id(trade.getConta().getId()).dataDeVencimento(null).nome(trade.getConta().getNome())
						.resultado(BigDecimal.ZERO).build();
				mapa.put(trade.getConta().getId(), resultado);
			}
			resultado.setResultado(resultado.getResultado().add(BigDecimal.valueOf(trade.getResultado())));
		}
		List<ContaResultadoAssinaturaResponse> retorno = new ArrayList<>();
		for (ContaResultadoAssinaturaResponse result : mapa.values()) {
			retorno.add(result);
		}
		Collections.sort(retorno, new Comparator<ContaResultadoAssinaturaResponse>() {
			@Override
			public int compare(ContaResultadoAssinaturaResponse o1, ContaResultadoAssinaturaResponse o2) {
				return StringUtils.stripAccents(o1.getNome())
						.compareToIgnoreCase(StringUtils.stripAccents(o2.getNome()));
			}
		});
		return retorno;
	}

	public ResultadoGlobalResponse getResultadoGlobal(String expert) {
		ResultadoGlobalResponse response = ResultadoGlobalResponse.builder().expert(expert).build();

		List<TradeResultadoDiaVO> resultadosDia = tradeResultadoDiaRespository.getList(expert);
		response.add(resultadosDia);
		
		response.atualizarAcumulados();

		return response;
	}

}
