package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {
	
	private String email;
	private String senha;
	private String redirect;

}
