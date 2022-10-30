package br.app.grid.wallet.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
