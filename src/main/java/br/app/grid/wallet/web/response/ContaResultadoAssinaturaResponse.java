package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaResultadoAssinaturaResponse {

	private String id;

	private String nome;
	private String corretora;
	private BigDecimal resultado;

	private LocalDate dataDeVencimento;

}
