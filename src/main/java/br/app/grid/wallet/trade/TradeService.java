package br.app.grid.wallet.trade;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return tradeRepository.getList(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

}
