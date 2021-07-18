package br.app.grid.wallet.cotacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoVO {

	private String codigo;
	private Double valor;
	private String nome;

	public String toString() {
		return codigo + ": " + valor+" - "+nome;
	}

}
