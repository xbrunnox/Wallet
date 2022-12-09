package br.app.grid.wallet.meta;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointPositions {
	
	private List<ContaPosicoesMT> contas;

}
