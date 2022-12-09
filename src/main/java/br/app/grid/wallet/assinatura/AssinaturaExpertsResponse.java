package br.app.grid.wallet.assinatura;

import java.util.List;

import br.app.grid.wallet.assinatura.view.AssinaturaExpertView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssinaturaExpertsResponse {
	
	private String conta;
	
	private List<AssinaturaExpertView> experts;

}
