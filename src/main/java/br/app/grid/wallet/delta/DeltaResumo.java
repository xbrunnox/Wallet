package br.app.grid.wallet.delta;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 16 de janeiro de 2022.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class DeltaResumo {

	private BigDecimal vwapSuperior;
	private BigDecimal vwapInferior;

	private BigDecimal amplitude;
	private BigDecimal delta;

	private List<Delta> deltas;
}
