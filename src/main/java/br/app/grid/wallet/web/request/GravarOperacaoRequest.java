package br.app.grid.wallet.web.request;

import java.math.BigDecimal;
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

	private String conta;

	private String expert;

	private String ativo;

	private BigDecimal volume;

	private Double preco;

	private String direcao;

	private String tipo;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime data;

}
