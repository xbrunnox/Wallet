package br.app.grid.wallet.posicao;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.carteira.Carteira;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Posicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_carteira")
	private Carteira carteira;
	
	@ManyToOne
	@JoinColumn(name="id_ativo")
	private Ativo ativo;
	
	private int quantidade;
	
	private BigDecimal strike;

}
