package br.app.grid.wallet.licenca;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicencaResponse {
	
	private String id;
	
	private Boolean ativo;

}
