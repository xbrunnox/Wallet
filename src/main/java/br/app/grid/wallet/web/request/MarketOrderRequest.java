package br.app.grid.wallet.web.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import br.app.grid.wallet.enums.DirecaoOperacaoEnum;
import br.app.grid.wallet.enums.TipoOperacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>MarketOrderRequest</b><br>
 * Request para envio de ordens a mercado.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de abril de 2023.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class MarketOrderRequest {
	
	private String conta;
	private String ativo;
	private String expert;
	
	private DirecaoOperacaoEnum direcao;
	
	private BigDecimal volume;
	
	private BigDecimal stopLoss;
	
	private BigDecimal takeProfit;
	
	private TipoOperacaoEnum tipo;
	
	private Long bilhete;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
	private LocalDateTime dataHora;

}
