package br.app.grid.wallet.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class ExpertUser {
	
	private String licenca;
	private String expiracao;
	private String nome;
	private String ip;
	private String dataDeConexao;
	private String ultimaComunicacao;
	private String expert;
	private String versao;
	private String tempoEnvio;

}
