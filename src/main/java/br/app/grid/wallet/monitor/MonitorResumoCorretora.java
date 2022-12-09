package br.app.grid.wallet.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorResumoCorretora {
	
	private String corretora;
	
	private int quantidade;

}
