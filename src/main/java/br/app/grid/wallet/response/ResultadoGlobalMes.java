package br.app.grid.wallet.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.app.grid.wallet.trade.vo.TradeResultadoDiaVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoGlobalMes implements Comparable<ResultadoGlobalMes> {

	private int mes;

	private int ano;

	private List<ResultadoGlobalDia> dias;

	private BigDecimal acumuladoMes;

	private BigDecimal acumulado;

	private BigDecimal acumuladoAnterior;

	private int positivos;

	private int negativos;

	@JsonIgnore
	private Map<Integer, ResultadoGlobalDia> mapaDias;

	public void add(TradeResultadoDiaVO resultadoDia) {
		if (dias == null)
			dias = new ArrayList<>();
		if (mapaDias == null)
			mapaDias = new HashMap<>();

		ResultadoGlobalDia globalDia = mapaDias.get(resultadoDia.getData().getDayOfMonth());
		if (Objects.isNull(globalDia)) {
			globalDia = ResultadoGlobalDia.builder().data(resultadoDia.getData()).resultado(BigDecimal.ZERO).build();
			dias.add(globalDia);
			mapaDias.put(resultadoDia.getData().getDayOfMonth(), globalDia);
		}

		globalDia.setResultado(globalDia.getResultado().add(resultadoDia.getResultado()));
	}

	@Override
	public int compareTo(ResultadoGlobalMes o) {
		if (ano < o.getAno())
			return -1;
		if (ano > o.getAno())
			return 1;
		return mes - o.getMes();
	}

	public BigDecimal atualizarAcumulado() {
		positivos = 0;
		negativos = 0;
		acumuladoMes = BigDecimal.ZERO;
		for (ResultadoGlobalDia resultadoDia : dias) {
			acumuladoMes = acumuladoMes.add(resultadoDia.getResultado());
			resultadoDia.setAcumulado(acumuladoAnterior.add(acumuladoMes));
			resultadoDia.setAcumuladoMes(acumuladoMes);
			if (resultadoDia.getResultado().compareTo(BigDecimal.ZERO) < 0) {
				negativos++;
			} else {
				positivos++;
			}
		}
		return acumuladoMes;
	}
}