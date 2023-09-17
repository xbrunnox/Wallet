package br.app.grid.wallet.estrategia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.ativo.Ativo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>EstrategiaAberturaFechamentoItem</b><br>
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 05 de setembro de 2023.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstrategiaAberturaFechamentoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ativo")
	private Ativo ativo;

	private BigDecimal gatilho;

	private BigDecimal stop;

	private int operacoes;

	private int acertos;

	private int erros;

	private BigDecimal taxaDeAcerto;

	private BigDecimal resultado;

	private BigDecimal resultadoPercentual;

	private LocalDateTime dataDeAtualizacao;

}
