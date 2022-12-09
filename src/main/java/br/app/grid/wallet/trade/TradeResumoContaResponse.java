package br.app.grid.wallet.trade;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeResumoContaResponse {
	
	private String conta;
	
	private String nome;
	
	private String corretora;
	
	private BigDecimal total;
	
	private int operacoes;

}
