package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PosicaoResponse {

	private String conta;

	private String nome;
	
	private String corretora;
	
	private String servidor;

	private BigDecimal volume;
	private String direcao;
	private String ativo;
	private BigDecimal abertura;
	private BigDecimal profit;
	private LocalDateTime data;

}
