package br.app.grid.wallet.backtest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.backtest.repository.BacktestOperacaoRepository;
import br.app.grid.wallet.backtest.vo.BacktestDia;
import br.app.grid.wallet.backtest.vo.BacktestDiaVO;
import br.app.grid.wallet.backtest.vo.BacktestMesAnoVO;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoVO;

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
      Map<Integer, BacktestMesAnoVO> mapaMesesDoAno =
          mapaAnos.get(operacao.getDataSaida().getYear());
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
      backMesAno.add(operacao.getDataSaida().toLocalDate(),
          operacao.getLucro().multiply(multiplicador));
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
      mes.atualizarEstatisticas();
    }
    return retorno;
  }

  public List<BacktestDiaVO> getDetalhesPorDia(Integer idBacktest, LocalDate dataInicial,
      LocalDate dataFinal) {
    System.out.println(Date.from(dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    List<BacktestDiaVO> retorno = new ArrayList<>();
    BacktestDiaVO ultimoDia = null;
    List<BacktestOperacaoVO> operacoes =
        operacaoRepository.getListVO(idBacktest, dataInicial, dataFinal);
    BigDecimal acumulado = BigDecimal.ZERO;
    BigDecimal acumuladoDia = BigDecimal.ZERO;
    for (BacktestOperacaoVO operacao : operacoes) {
      if (Objects.isNull(ultimoDia) || !operacao.getDataEntrada().equals(ultimoDia.getData())) {
        ultimoDia =
            BacktestDiaVO.builder().data(operacao.getDataEntrada()).acumulado(BigDecimal.ZERO)
                .operacoes(new ArrayList<>()).resultado(BigDecimal.ZERO).build();
        retorno.add(ultimoDia);
        acumuladoDia = BigDecimal.ZERO;
      }
      ultimoDia.add(operacao);
      ultimoDia.setResultado(ultimoDia.getResultado().add(operacao.getLucro()));
      acumulado = acumulado.add(operacao.getLucro());
      ultimoDia.setAcumulado(acumulado);
      acumuladoDia = acumuladoDia.add(operacao.getLucro());
      operacao.setAcumulado(acumuladoDia);
    }
    for (BacktestDiaVO dia : retorno) {
      dia.atualizarEstatisticas();
    }
    return retorno;
  }

  public void gravar(BacktestOperacao operacao) {
    operacaoRepository.save(operacao);

  }

  public void gravar(List<BacktestOperacao> operacoes) {
    for (BacktestOperacao operacao : operacoes)
      gravar(operacao);

  }

}
