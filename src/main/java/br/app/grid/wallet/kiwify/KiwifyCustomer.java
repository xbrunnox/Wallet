package br.app.grid.wallet.kiwify;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KiwifyCustomer {
	private String full_name; // : "Mario Chase",
	private String email; // : "test@example.com",
	private String mobile; // : null,
	@JsonAlias(value = "CPF")
	private String cpf; // : null,
	private String ip; // : "192.168.0.1"

}
