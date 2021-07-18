package br.app.grid.wallet.resumo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResumoPosicao {

	private int quantidade;

	private String ativo;

	private BigDecimal strike;

	private BigDecimal valor;

	private BigDecimal total;
	
	private BigDecimal percentual;

}
