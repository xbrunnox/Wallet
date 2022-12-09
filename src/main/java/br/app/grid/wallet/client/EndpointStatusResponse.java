package br.app.grid.wallet.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointStatusResponse {
	
	private String tempoEnvio;
	
	private List<ClienteUser> onlineUsers;
	
	private List<ExpertUser> onlineExperts;

}
