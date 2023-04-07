package br.app.grid.wallet.meta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import br.app.grid.wallet.enums.DirecaoOperacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperacaoMT {

	private String ativo;
	private BigDecimal volume;
	private DirecaoOperacaoEnum direcao;
	private BigDecimal preco;
	private Long bilhete;
	private Long magic;
	private String expert;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
	private LocalDateTime horario;

}
