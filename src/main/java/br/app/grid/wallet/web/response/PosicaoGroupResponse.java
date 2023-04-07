package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>PosicaoGroupResponse</b><br>
 * @since 13 de março de 2023.
 * @author Brunno José Guimarães de Almeida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoGroupResponse {
	
	private String ativo;
	
	private BigDecimal volume;
	
	private String direcao;
	
	private String titulo;
	
	private List<PosicaoResponse> posicoes;

	public void add(PosicaoResponse posicaoResponse) {
		posicoes.add(posicaoResponse);
	}

}
