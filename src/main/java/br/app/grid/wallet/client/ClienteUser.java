package br.app.grid.wallet.client;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
public class ClienteUser {

	private String licenca;
	private String expiracao;
	private String nome;
	private String corretora;
	private String ip;
	private LocalDateTime dataDeConexao;
	private LocalDateTime ultimaComunicacao;
	private String versao;
	private String tempoEnvio;
	private Boolean pausado;
	private String servidor;
	private String maquina;

}
