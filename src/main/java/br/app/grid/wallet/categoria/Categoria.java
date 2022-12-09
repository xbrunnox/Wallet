package br.app.grid.wallet.categoria;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
	
	@Id
	private Integer id;
	
	private String nome;
	
	private Double ganho;

}
