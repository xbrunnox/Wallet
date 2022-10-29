package br.app.grid.wallet.socket;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouterStatusResponse {
	
	private String tempoEnvio;
	
	private List<SocketUser> onlineUsers;

}
