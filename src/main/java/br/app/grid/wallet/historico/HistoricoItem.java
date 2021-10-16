package br.app.grid.wallet.historico;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class HistoricoItem {

	private int mes;

	private int ano;

	private BigDecimal previsto;

	private BigDecimal realizado;
	
	private BigDecimal baseDeCalculo;
	
	private BigDecimal rendimentoPercentual;

	private BigDecimal rendimentoPrevisto;

	private BigDecimal rendimento;

	private BigDecimal aporte;

	private BigDecimal aporteAcumulado;

	private BigDecimal rendimentoAcumulado;

	public void addAporte(BigDecimal valor) {
		if (aporte != null)
			aporte = aporte.add(valor).setScale(2);
		else
			aporte = valor.setScale(2);
	}

	

}
