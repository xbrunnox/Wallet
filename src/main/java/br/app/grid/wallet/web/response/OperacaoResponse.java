package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperacaoResponse {
	
	private int id;
	
	private String conta;
	
	private String nome;
	
	private String expert;
	
	private String ativo;
	
	private String categoria;
	
	private Double preco;
	
	private BigDecimal volume;
	
	private String direcao;
	
	private LocalDateTime data;
	
	private Boolean online;

}
