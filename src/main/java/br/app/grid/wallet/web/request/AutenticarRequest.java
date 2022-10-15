package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutenticarRequest {
	private String nome;
	
	private String corretora;
	
	private Integer conta;
}
