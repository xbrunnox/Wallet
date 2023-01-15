package br.app.grid.wallet.backtest.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BacktestMesAnoVO {

	private int mes;

	private int ano;

	private BigDecimal total;

	private List<BacktestDia> dias;

	private String descricao;

	private int acertos;
	private int erros;

	private BigDecimal taxaDeAcerto;

	public void add(LocalDate localDate, BigDecimal lucro) {
		if (dias == null)
			dias = new ArrayList<>();
		for (BacktestDia dia : dias) {
			if (localDate.equals(dia.getData())) {
				if (total == null)
					total = BigDecimal.ZERO;
				total = total.add(lucro);
				dia.add(lucro);
				if (lucro.compareTo(BigDecimal.ZERO) >= 0)
					acertos++;
				else
					erros++;
				atualizarTaxaDeAcerto();
				return;
			}
		}
		dias.add(BacktestDia.builder().data(localDate).saldo(lucro).build());
		if (total == null)
			total = BigDecimal.ZERO;
		total = total.add(lucro);
		if (lucro.compareTo(BigDecimal.ZERO) >= 0)
			acertos++;
		else
			erros++;
		atualizarTaxaDeAcerto();
	}

	private void atualizarTaxaDeAcerto() {
		if (acertos == 0 && erros == 0)
			taxaDeAcerto = BigDecimal.ZERO;
		else
			taxaDeAcerto = BigDecimal.valueOf((acertos * 100) / (acertos + erros));
	}

}
