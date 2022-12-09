package br.app.grid.wallet.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.monitor.MonitorResumo;
import br.app.grid.wallet.monitor.MonitorResumoCorretora;
import br.app.grid.wallet.util.CorretoraUtil;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

	@Autowired
	private EndpointService endpointService;

	@GetMapping("/resumo")
	public MonitorResumo resumo() {
		EndpointStatusResponse endpointStatus = endpointService.getStatus();
		Map<String, MonitorResumoCorretora> mapaCorretoras = new HashMap<>();
		for (ClienteUser client : endpointStatus.getOnlineUsers()) {
			MonitorResumoCorretora resumoCorretora = mapaCorretoras.get(CorretoraUtil.format(client.getCorretora()));
			if (resumoCorretora == null) {
				resumoCorretora = MonitorResumoCorretora.builder().quantidade(0)
						.corretora(CorretoraUtil.format(client.getCorretora())).build();
				mapaCorretoras.put(CorretoraUtil.format(client.getCorretora()), resumoCorretora);
			}
			resumoCorretora.setQuantidade((resumoCorretora.getQuantidade() + 1));
		}

		return MonitorResumo.builder().usuariosOnline(endpointStatus.getOnlineUsers().size())
				.expertsOnline(endpointStatus.getOnlineExperts().size())
				.corretoras(new ArrayList<>(mapaCorretoras.values())).horario(LocalDateTime.now()).build();
	}

}
