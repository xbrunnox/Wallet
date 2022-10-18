package br.app.grid.wallet.robo;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Robo {

	@Id
	private String id;

	private String ativo;

	private Double volume;

	private Double alvo;
	
	private Double stop;
	
	private Double tolerancia;
	
	private int tentativas;

	private int timeframe;


}
