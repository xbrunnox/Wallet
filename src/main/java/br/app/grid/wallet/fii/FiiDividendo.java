package br.app.grid.wallet.fii;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.app.grid.wallet.dividendo.Dividendo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiiDividendo {

	private String ativo;

	private LocalDate dataBase;

	private LocalDate dataPagamento;

	private BigDecimal valor;
	
	private BigDecimal yield;

	public FiiDividendo(Dividendo dividendo) {
		this.ativo = dividendo.getAtivo().getCodigo();
		this.dataBase = dividendo.getDataBase();
		this.dataPagamento = dividendo.getDataPagamento();
		this.valor = dividendo.getValor();
		this.yield = dividendo.getYield();
	}
}
