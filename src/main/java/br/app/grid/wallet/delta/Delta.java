package br.app.grid.wallet.delta;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class Delta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate data;

	private BigDecimal vwapSuperior;
	private BigDecimal vwapInferior;

	private BigDecimal minima;
	private BigDecimal maxima;
	
	private BigDecimal amplitude;
	private BigDecimal delta;

	private String periodo;

}
