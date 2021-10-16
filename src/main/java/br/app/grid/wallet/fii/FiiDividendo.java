package br.app.grid.wallet.fii;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FiiDividendo {
	
	public LocalDate dataBase;

	public LocalDate dataPagamento;
	
	public Double valor;
}
