package br.app.grid.wallet.indicador.rsi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.app.grid.wallet.exception.BusinessException;
import br.app.grid.wallet.indicador.Rsi;
import br.app.grid.wallet.web.request.RegistrarRsiRequest;

/**
 * @author Brunon José Guimarães de Almeida.
 * @since 30 de maio de 2023.
 */
@Service
public class RsiService {

	private Map<String, Rsi> mapaRsi = new HashMap<>();

	/**
	 * Retorna o RSI do ativo indicado.
	 * 
	 * @param ativo Ativo.
	 * @return Rsi.
	 */
	public Rsi getRsi(String ativo) {
		if (!mapaRsi.containsKey(ativo))
			throw new BusinessException("Sem dados de RSI para o ativo.");
		return mapaRsi.get(ativo);
	}

	/**
	 * Realiza o registro do valor de Rsi.
	 * 
	 * @param request Request.
	 * @return RSI.
	 */
	public Rsi registrar(RegistrarRsiRequest request) {
		Rsi rsi = null;
		if (mapaRsi.containsKey(request.getAtivo())) {
			rsi = mapaRsi.get(request.getAtivo());
			rsi.setRsi(request.getRsi());
		} else {
			rsi = Rsi.builder().ativo(request.getAtivo()).rsi(request.getRsi()).build();
			mapaRsi.put(request.getAtivo(), rsi);
		}
		return rsi;
	}
}
