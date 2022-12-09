package br.app.grid.wallet.licenca;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ContaResultadoView {

	@Id
	private String id;
	
	private String nome;
	private String corretora;
	private Double resultado;

}
