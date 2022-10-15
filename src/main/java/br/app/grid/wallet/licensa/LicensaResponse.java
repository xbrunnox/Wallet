package br.app.grid.wallet.licensa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicensaResponse {
	
	private String id;
	
	private Boolean ativo;

}
