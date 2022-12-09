package br.app.grid.wallet.ativo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.categoria.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ativo {
	
	@Id
	private String codigo;

	@ManyToOne
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;

}
