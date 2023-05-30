package br.app.grid.wallet.delta.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.delta.Delta;
import br.app.grid.wallet.delta.DeltaResumo;
import br.app.grid.wallet.delta.repository.DeltaRepository;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 16 de janeiro de 2022.
 */
@Service
public class OldDeltaService {

	@Autowired
	private DeltaRepository repository;

	/**
	 * Realiza a gravação do delta indicado.
	 * 
	 * @param delta Delta.
	 * @return Delta após gravação.
	 */
	public Delta save(Delta delta) {
		delta.setAmplitude(delta.getMaxima().subtract(delta.getMinima()));
		if (delta.getVwapInferior().compareTo(delta.getVwapSuperior()) > 0) {
			delta.setDelta(delta.getVwapInferior());
		} else {
			delta.setDelta(delta.getVwapSuperior());
		}
		repository.save(delta);
		return delta;
	}

	public List<Delta> getDelta(int periodos) {
		PageRequest page = PageRequest.of(0, periodos);
		List<Delta> deltas = repository.getList(page);
		return deltas;
	}

	public DeltaResumo getResumo(int periodos) {
		List<Delta> deltas = getDelta(periodos);
		
		BigDecimal vwapSuperior = BigDecimal.ZERO;
		BigDecimal vwapInferior = BigDecimal.ZERO;
		BigDecimal amplitude = BigDecimal.ZERO;
		BigDecimal delta = BigDecimal.ZERO;

		for (Delta deltaDia : deltas) {
			vwapSuperior = vwapSuperior.add(deltaDia.getVwapSuperior());
			vwapInferior = vwapInferior.add(deltaDia.getVwapInferior());
			amplitude = amplitude.add(deltaDia.getAmplitude());
			delta = delta.add(deltaDia.getDelta());
		}
		BigDecimal divisor = BigDecimal.valueOf(periodos);
		return DeltaResumo.builder().amplitude(amplitude.divide(divisor, BigDecimal.ROUND_DOWN))
				.delta(delta.divide(divisor, BigDecimal.ROUND_DOWN))
				.vwapInferior(vwapInferior.divide(divisor, BigDecimal.ROUND_DOWN))
				.vwapSuperior(vwapSuperior.divide(divisor, BigDecimal.ROUND_DOWN)).deltas(deltas).build();
	}

}
