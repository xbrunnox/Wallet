package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacktestDia {

	private LocalDate data;

	private BigDecimal saldo;
	
	private BigDecimal acumulado;
	
	private BigDecimal acumuladoGeral;

	public void add(BigDecimal valor) {
		if (saldo == null)
			saldo = BigDecimal.ZERO;
		saldo = saldo.add(valor);
	}

}
