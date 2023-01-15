package br.app.grid.wallet.backtest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacktestOperacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_backtest")
	private Backtest backtest;
	
	private String direcao;
	
	private LocalDateTime dataEntrada;
	
	private LocalDateTime dataSaida;
	
	private BigDecimal volume;
	private BigDecimal precoEntrada;
	private BigDecimal precoSaida;
	private BigDecimal lucro;
	
	private int duracao;

}
