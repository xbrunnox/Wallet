package br.app.grid.wallet.estrategia.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.enums.TimeFrameEnum;
import br.app.grid.wallet.estrategia.EstrategiaAberturaFechamentoItem;
import br.app.grid.wallet.estrategia.repository.EstrategiaAberturaFechamentoItemRepository;

/**
 * <b>EstrategiaService</b><br>
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 05 de setembro de 2023.
 */
@Service
public class EstrategiaAberturaFechamentoService {

	@Autowired
	private EstrategiaAberturaFechamentoItemRepository estrategiaAberturaFechamentoItemRepository;

	@Autowired
	private CandleService candleService;

	public EstrategiaAberturaFechamentoItem save(EstrategiaAberturaFechamentoItem item) {
		return estrategiaAberturaFechamentoItemRepository.save(item);
	}

	public List<EstrategiaAberturaFechamentoItem> getList() {
		return estrategiaAberturaFechamentoItemRepository.getList();
	}

	public void atualizarEstatisticas() {
		LocalDate dataInicial = LocalDate.of(2023, 1, 1);
		LocalDate dataFinal = LocalDate.now();
		List<EstrategiaAberturaFechamentoItem> estrategiaItens = getList();
		for (EstrategiaAberturaFechamentoItem estrategiaItem : estrategiaItens) {
			List<Candle> candles = candleService.getList(estrategiaItem.getAtivo().getCodigo(), TimeFrameEnum.D1,
					dataInicial, dataFinal);
			BigDecimal volumeMedio = BigDecimal.ZERO;
			for (Candle candle : candles) {
				volumeMedio = volumeMedio.add(candle.getAbertura().multiply(BigDecimal.valueOf(candle.getVolume())));
			}
			if (candles.size() != 0) {
				volumeMedio = volumeMedio.divide(BigDecimal.valueOf(candles.size()), 2, RoundingMode.HALF_DOWN);
			}

			int acertos = 0;
			int erros = 0;
			int operacoes = 0;
			List<BigDecimal> resultados = new ArrayList<>();
			Candle candleAnterior = null;
			BigDecimal percentualGanho = BigDecimal.ZERO;
			BigDecimal totalLucro = BigDecimal.ZERO;

			for (Candle candle : candles) {
				if (candleAnterior != null) {
					BigDecimal precoGatilho = candleAnterior.getFechamento()
							.multiply(BigDecimal.valueOf(100).add(estrategiaItem.getGatilho()))
							.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);

					if (candle.getAbertura().compareTo(precoGatilho) < 0
							|| candle.getMinima().compareTo(precoGatilho) < 0) {
						// Houve entrada
						BigDecimal entrada = (candle.getAbertura().compareTo(precoGatilho) < 0 ? candle.getAbertura()
								: precoGatilho);

						BigDecimal saida = candle.getFechamento();
						if (estrategiaItem.getStop() != null) {
							// Tem stop
							BigDecimal stop = entrada
									.multiply(BigDecimal.valueOf(1).subtract(estrategiaItem.getStop()));
							if (candle.getMinima().compareTo(stop) < 0) {
								saida = stop;
							}
						} else {
							// Não tem stop
						}
						BigDecimal lucro = saida.subtract(entrada);
						resultados.add(lucro);
						totalLucro = totalLucro.add(lucro);
						percentualGanho = percentualGanho.add(saida.divide(entrada, 2, RoundingMode.HALF_DOWN)
								.subtract(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(100)));
						operacoes++;
						if (lucro.compareTo(BigDecimal.ZERO) >= 0) {
							acertos++;
						} else {
							erros++;
						}
					}
				}
				candleAnterior = candle;
			}
			BigDecimal taxaDeAcerto = BigDecimal.ZERO;
			if (operacoes > 0)
				taxaDeAcerto = BigDecimal.valueOf(estrategiaItem.getAcertos()).multiply(BigDecimal.valueOf(100))
						.divide(BigDecimal.valueOf(operacoes), 0, RoundingMode.HALF_UP);
			estrategiaItem.setErros(erros);
			estrategiaItem.setAcertos(acertos);
			estrategiaItem.setOperacoes(operacoes);
			estrategiaItem.setDataDeAtualizacao(LocalDateTime.now());
			estrategiaItem.setResultadoPercentual(percentualGanho);
			estrategiaItem.setResultado(totalLucro);
			estrategiaItem.setTaxaDeAcerto(taxaDeAcerto);
			estrategiaAberturaFechamentoItemRepository.save(estrategiaItem);
			System.out.println("Gravando");
			try {
				System.out.println(new ObjectMapper().writeValueAsString(estrategiaItem));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
