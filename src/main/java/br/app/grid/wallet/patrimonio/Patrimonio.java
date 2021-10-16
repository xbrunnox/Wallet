package br.app.grid.wallet.patrimonio;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.carteira.Carteira;
import lombok.Data;

@Entity
@Data
public class Patrimonio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_carteira")
	private Carteira carteira;
	
	private LocalDate data;
	
	private BigDecimal valor;

}
