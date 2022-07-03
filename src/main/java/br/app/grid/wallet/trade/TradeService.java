package br.app.grid.wallet.trade;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

	@Autowired
	private TradeRepository tradeRepository;

	public void gravar(Trade trade) {
		trade.setData(LocalDateTime.now());
		tradeRepository.save(trade);
	}

}
