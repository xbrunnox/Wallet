package br.app.grid.wallet.trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.robo.Robo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 16 de janeiro de 2022.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;

	@ManyToOne
	@JoinColumn(name = "expert")
	private Robo expert;

	@ManyToOne
	@JoinColumn(name = "ativo")
	private Ativo ativo;

	private String direcao;

	private BigDecimal volume;

	private Double compra;

	private Double venda;

	private Double pontos;

	private LocalDateTime dataEntrada;

	private LocalDateTime dataSaida;

	private Long duracao;

	private Double resultado;

	public String getDuracaoFormatada() {
		if (duracao < 60) {
			return duracao + " s";
		} else if (duracao < 3600) {
			return duracao / 60 + "m" + (duracao % 60) + "s";
		}
		return duracao / 3600 + "h" + ((duracao % 3600) / 60) + "m" + (duracao % 60) + "s";
	}

}
