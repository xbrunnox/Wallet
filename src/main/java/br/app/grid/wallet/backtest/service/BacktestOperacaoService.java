package br.app.grid.wallet.backtest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.backtest.repository.BacktestOperacaoRepository;
import br.app.grid.wallet.backtest.vo.BacktestDia;
import br.app.grid.wallet.backtest.vo.BacktestMesAnoVO;

@Service
public class BacktestOperacaoService {

	@Autowired
	private BacktestOperacaoRepository operacaoRepository;

	public List<BacktestMesAnoVO> getAcumulado(Backtest backtest, BigDecimal multiplicador) {
		List<BacktestMesAnoVO> retorno = new ArrayList<>();
		Map<Integer, Map<Integer, BacktestMesAnoVO>> mapaAnos = new HashMap<>();
		List<BacktestOperacao> operacoes = operacaoRepository.getList(backtest.getId());
		for (BacktestOperacao operacao : operacoes) {
			// Identificação do ano
			Map<Integer, BacktestMesAnoVO> mapaMesesDoAno = mapaAnos.get(operacao.getDataSaida().getYear());
			if (mapaMesesDoAno == null) {
				mapaMesesDoAno = new HashMap<>();
				mapaAnos.put(operacao.getDataSaida().getYear(), mapaMesesDoAno);
			}
			// Identificação do mês
			BacktestMesAnoVO backMesAno = mapaMesesDoAno.get(operacao.getDataSaida().getMonthValue());
			if (backMesAno == null) {
				backMesAno = BacktestMesAnoVO.builder().ano(operacao.getDataSaida().getYear())
						.descricao(backtest.getDescricao()).mes(operacao.getDataSaida().getMonthValue())
						.total(BigDecimal.ZERO).build();
				mapaMesesDoAno.put(operacao.getDataSaida().getMonthValue(), backMesAno);
				retorno.add(backMesAno);
			}
			backMesAno.add(operacao.getDataSaida().toLocalDate(), operacao.getLucro().multiply(multiplicador));
		}
		BigDecimal acumuladoGeral = BigDecimal.ZERO;
		for (BacktestMesAnoVO mes : retorno) {
			BigDecimal acumulado = BigDecimal.ZERO;
			for (BacktestDia dia : mes.getDias()) {
				acumulado = acumulado.add(dia.getSaldo());
				acumuladoGeral = acumuladoGeral.add(dia.getSaldo());
				dia.setAcumulado(acumulado);
				dia.setAcumuladoGeral(acumuladoGeral);
			}
		}
		return retorno;
	}

}
