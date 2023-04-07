package br.app.grid.wallet.trade.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name="trade_view")
@NoArgsConstructor
@AllArgsConstructor
public class TradeVO {

	@Id
	private Long id;

	private String conta;

	private String nome;

	private String corretora;

	private String expert;

	private String ativo;

	private String direcao;
	
	private String servidor;

	private BigDecimal volume;

	private Double compra;

	private Double venda;

	private Double pontos;

	private LocalDateTime dataEntrada;

	private LocalDateTime dataSaida;

	private Long duracao;

	private BigDecimal resultado;

	public String getDuracaoFormatada() {
		if (duracao < 60) {
			return duracao + " s";
		} else if (duracao < 3600) {
			return duracao / 60 + "m" + (duracao % 60) + "s";
		}
		return duracao / 3600 + "h" + ((duracao % 3600) / 60) + "m" + (duracao % 60) + "s";
	}

}
