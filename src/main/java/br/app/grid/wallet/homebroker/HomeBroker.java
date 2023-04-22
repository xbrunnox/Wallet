package br.app.grid.wallet.homebroker;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HomeBroker {

	@Id
	private Integer id;
	
	private String automacao;
	
	private String ativo;
	
	private String conta;
	
	private BigDecimal volume;
	
	private BigDecimal stopLoss;
	
	private BigDecimal takeProfit;

}
