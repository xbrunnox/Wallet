package br.app.grid.wallet.maquina;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Maquina {
	
	@Id
	private int id;
	
	private String nome;

}
