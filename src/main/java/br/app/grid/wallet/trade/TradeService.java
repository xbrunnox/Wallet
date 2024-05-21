package br.app.grid.wallet.trade;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.trade.repository.TradeRepository;
import br.app.grid.wallet.trade.vo.TradeVO;

@Service
public class TradeService {

  @Autowired
  private TradeRepository tradeRepository;

  public void gravar(Trade trade) {
    tradeRepository.save(trade);
  }

  public List<Trade> getList(String conta) {
    return tradeRepository.getList(conta);
  }

  /**
   * Retorna os trades realizados na data indicada.
   * 
   * @param data Data.
   * @return Lista de trades.
   */
  public List<Trade> getList(LocalDate data) {
    return tradeRepository
        .getList(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  public List<Trade> getList(String conta, LocalDate data) {
    return tradeRepository.getList(conta,
        Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  public void excluir(Trade trade) {
    tradeRepository.delete(trade);

  }

  public List<Trade> getListByExpert(String expert) {
    return tradeRepository.getListByExpert(expert);
  }

  public List<TradeVO> getListVO(LocalDate data) {
    return tradeRepository.getListVO(null, null,
        Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  /**
   * Retorna a lista de trades realizado pelo afiliado na data indicada.
   * 
   * @param afiliado Afiliado.
   * @param data Data.
   * @return Lista de trades.
   */
  public List<TradeVO> getListVO(Afiliado afiliado, LocalDate data) {
    if (Objects.isNull(afiliado)) {
      return new ArrayList<>();
    }
    return tradeRepository.getListVO(afiliado.getId(), null,
        Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

}
