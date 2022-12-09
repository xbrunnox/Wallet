package br.app.grid.wallet.meta;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoMT {

	private BigDecimal volume;
	private String direcao;	
	private String ativo;
	private BigDecimal abertura;
	private BigDecimal profit;
	private String data;
	
}
