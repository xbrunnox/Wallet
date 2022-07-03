package br.app.grid.wallet.dividendo;

import java.math.BigDecimal;
import java.time.LocalDate;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dividendo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="id_ativo")
	private Ativo ativo;
	
	private LocalDate dataBase;

	private LocalDate dataPagamento;
	
	private LocalDateTime dataCadastro;
	
	private BigDecimal valor;
	
	private BigDecimal yield;

}
