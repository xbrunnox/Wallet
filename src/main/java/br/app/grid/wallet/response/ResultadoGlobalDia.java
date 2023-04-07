package br.app.grid.wallet.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoGlobalDia {
	
	private LocalDate data;
	
	private BigDecimal resultado;
	
	private BigDecimal acumulado;
	
	private BigDecimal acumuladoMes;

}
