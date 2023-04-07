package br.app.grid.wallet.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.app.grid.wallet.trade.vo.TradeResultadoDiaVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <b>ResultadoGlobalResponse</b>
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
@Data
@Builder
@AllArgsConstructor
public class ResultadoGlobalResponse {

	private String expert;

	private List<ResultadoGlobalMes> meses;

	private BigDecimal acumulado;

	private int positivos;

	private int negativos;

	public void add(List<TradeResultadoDiaVO> resultadosDia) {
		if (meses == null)
			meses = new ArrayList<>();

		Map<String, ResultadoGlobalMes> mapaMeses = new HashMap<>();
		for (ResultadoGlobalMes globalMes : meses) {
			mapaMeses.put(globalMes.getAno() + "-" + globalMes.getMes(), globalMes);
		}

		for (TradeResultadoDiaVO resultadoDia : resultadosDia) {
			ResultadoGlobalMes globalMes = mapaMeses
					.get(resultadoDia.getData().getYear() + "-" + resultadoDia.getData().getMonthValue());
			if (globalMes == null) {
				globalMes = ResultadoGlobalMes.builder().mes(resultadoDia.getData().getMonthValue())
						.ano(resultadoDia.getData().getYear()).build();
				mapaMeses.put(resultadoDia.getData().getYear() + "-" + resultadoDia.getData().getMonthValue(),
						globalMes);
				meses.add(globalMes);
			}
			globalMes.add(resultadoDia);
		}
	}

	public void atualizarAcumulados() {
		Collections.sort(meses);
		BigDecimal saldo = BigDecimal.ZERO;
		for (ResultadoGlobalMes resultadoMes : meses) {
			resultadoMes.setAcumuladoAnterior(saldo);
			saldo = saldo.add(resultadoMes.atualizarAcumulado());
			resultadoMes.setAcumulado(saldo);
			positivos += resultadoMes.getPositivos();
			negativos += resultadoMes.getNegativos();
		}
		acumulado = saldo;
	}
}
