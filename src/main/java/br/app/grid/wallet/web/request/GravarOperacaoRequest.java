package br.app.grid.wallet.web.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GravarOperacaoRequest {

	private String licenca;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataDeEntrada;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataDeSaida;

	private String ativo;

	private Double volume;

	private Double entrada;

	private Double saida;

	private Double pontos;

	private Double valor;
	
	private String direcao;

}
