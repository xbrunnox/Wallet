package br.app.grid.wallet.commons;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AccountInfoMeta</b><br>
 * Representa as informações de conta do Metatrader.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 16 de março de 2023.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Builder
public class AccountInfoMeta {

	private String conta;
	private String nome;
	private String corretora;
	
	private BigDecimal balance;
	private BigDecimal equity;
	private BigDecimal profit;
	
	private String type;
	
	private Boolean algotrading;

}
