package br.app.grid.wallet.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.monitor.MonitorResumo;
import br.app.grid.wallet.monitor.MonitorResumoCorretora;
import br.app.grid.wallet.monitor.MonitorResumoServidor;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.util.CorretoraUtil;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

  @Autowired
  private RouterService routerService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping("/resumo")
  public MonitorResumo resumo() {
    EndpointStatusResponse endpointStatus = routerService.getStatus(request);
    Map<String, MonitorResumoCorretora> mapaCorretoras = new HashMap<>();
    Map<String, MonitorResumoServidor> mapaServidores = new HashMap<>();

    for (ClienteUser client : endpointStatus.getOnlineUsers()) {

      // Corretoras
      MonitorResumoCorretora resumoCorretora =
          mapaCorretoras.get(CorretoraUtil.format(client.getCorretora()));
      if (resumoCorretora == null) {
        resumoCorretora = MonitorResumoCorretora.builder().quantidade(0)
            .corretora(CorretoraUtil.format(client.getCorretora())).build();
        mapaCorretoras.put(CorretoraUtil.format(client.getCorretora()), resumoCorretora);
      }
      resumoCorretora.setQuantidade((resumoCorretora.getQuantidade() + 1));

      // Servidor
      String serverName =
          client.getServidor().substring(0, 1) + client.getServidor().substring(1).toLowerCase();
      MonitorResumoServidor resumoServidor = mapaServidores.get(serverName);
      if (resumoServidor == null) {
        resumoServidor = MonitorResumoServidor.builder().quantidade(0).servidor(serverName).build();
        mapaServidores.put(serverName, resumoServidor);
      }
      resumoServidor.setQuantidade((resumoServidor.getQuantidade() + 1));
    }

    return MonitorResumo.builder().usuariosOnline(endpointStatus.getOnlineUsers().size())
        .expertsOnline(endpointStatus.getOnlineExperts().size())
        .corretoras(new ArrayList<>(mapaCorretoras.values()))
        .experts(endpointStatus.getOnlineExperts())
        .servidores(new ArrayList<>(mapaServidores.values())).horario(LocalDateTime.now()).build();
  }

}
