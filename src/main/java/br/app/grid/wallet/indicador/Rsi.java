package br.app.grid.wallet.indicador;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 30 de maio de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rsi {
	
	private String ativo;
	
	private BigDecimal rsi;

}
