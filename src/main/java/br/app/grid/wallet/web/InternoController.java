package br.app.grid.wallet.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.enums.ServidorTipoEnum;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.servidor.ServidorService;
import br.app.grid.wallet.util.VersatilUtil;
import br.app.grid.wallet.web.response.ExpertStatusResponse;
import br.app.grid.wallet.web.response.StatusResumo;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 24 de janeiro de 2024.
 */
@RestController
@RequestMapping("/interno")
public class InternoController {

  @Autowired
  private ServidorService servidorService;

  @Autowired
  private EndpointService endpointService;

  @Autowired
  private RouterService routerService;

  @Autowired
  private OperacaoService operacaoService;

  @Autowired
  private RoboService expertService;

  @GetMapping("/status-resumo")
  public StatusResumo statusResumido() {
    List<Operacao> operacoes = operacaoService.getList();
    Set<String> contasEmOperacao = new HashSet<String>();

    for (Operacao operacao : operacoes) {
      contasEmOperacao.add(operacao.getConta().getId());
    }

    StatusResumo status = StatusResumo.builder().clientes(new HashSet<>())
        .experts(new ArrayList<>()).numeroClientesOnline(0).numeroExpertsOnline(0)
        .numeroServidoresOnline(0).servidores(new HashSet<>()).build();
    List<Servidor> servidores = servidorService.getList(ServidorTipoEnum.ENDPOINT_CUSTOMER, true);
    for (Servidor servidor : servidores) {
      try {
        EndpointStatusResponse endpointStatus = endpointService.status(servidor.getHostname());
        List<ClienteUser> users = endpointStatus.getOnlineUsers();
        for (ClienteUser cliente : users) {
          status.getClientes().add(cliente.getLicenca());
        }
        status.getServidores().add(servidor.getNome());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    try {
      ExpertStatusResponse expertStatus = routerService.getExpertStatus();
      List<br.app.grid.wallet.client.ExpertUser> users = expertStatus.getOnlineExperts();
      for (br.app.grid.wallet.client.ExpertUser cliente : users) {
        Robo expert = expertService.getById(cliente.getExpert());
        if (!Objects.isNull(expert.getAfiliado())
            && Integer.valueOf(1).equals(expert.getAfiliado().getId())) {
          status.getExperts().add(cliente.getExpert());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    status.setNumeroClientesOnline(status.getClientes().size());
    status.setNumeroServidoresOnline(status.getServidores().size());
    status.setNumeroEmOperacao(contasEmOperacao.size());
    status.setData(LocalDate.now().toString());
    status.setHorario(VersatilUtil.getHoursAndMinutes());
    return status;
  }

}
