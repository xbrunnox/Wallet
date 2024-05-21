package br.app.grid.wallet.router;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.commons.AccountInfoMeta;
import br.app.grid.wallet.enums.DirecaoOperacaoEnum;
import br.app.grid.wallet.meta.AccountInfosMT;
import br.app.grid.wallet.meta.ContaHistoricoMT;
import br.app.grid.wallet.meta.ContaPosicoesMT;
import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.meta.PosicaoMT;
import br.app.grid.wallet.socket.DataConverter;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.Constantes;
import br.app.grid.wallet.web.request.CandlesRequest;
import br.app.grid.wallet.web.request.MarketOrderRequest;
import br.app.grid.wallet.web.response.AtivoCandlesResponse;
import br.app.grid.wallet.web.response.ExpertStatusResponse;

/**
 * <b>RouterService</b><br>
 * Service responsável pela comunicação com o Router.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Service
public class RouterService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private AssinaturaService assinaturaService;

  public ContaHistoricoMT getHistoricoMetatrader(String conta, LocalDate dataInicial,
      LocalDate dataFinal) {
    Assinatura assinatura = assinaturaService.getList(conta).get(0);
    ContaHistoricoMT response = restTemplate
        .getForObject("http://" + assinatura.getServidor().getHostname() + ":8080/router/historico/"
            + conta + "/" + dataInicial + "/" + dataFinal, ContaHistoricoMT.class);
    return response;
  }

  public EndpointPositions getPosicoes() {
    EndpointPositions response =
        restTemplate.getForObject(Constantes.ROUTER_POSITIONS_URL, EndpointPositions.class);
    // TODO Remover
    for (ContaPosicoesMT conta : response.getContas()) {
      if (conta != null && conta.getPosicoes() != null) {
        for (int i = conta.getPosicoes().size() - 1; i >= 0; i--) {
          PosicaoMT posicao = conta.getPosicoes().get(i);
          if (!posicao.getAtivo().startsWith("WIN") && !posicao.getAtivo().startsWith("WDO")) {
            conta.getPosicoes().remove(i);
          }
        }
      }
    }
    return response;
  }

  /**
   * Retorna o Status dos clientes conectados.
   * 
   * @param request Http Request.
   * 
   * @return Status.
   */
  public EndpointStatusResponse getStatus(HttpServletRequest request) {
    EndpointStatusResponse response =
        restTemplate.getForObject(Constantes.ROUTER_STATUS_URL, EndpointStatusResponse.class);

    // Realiza a filtragem dos Clientes que pertencem ao afiliado.
    List<ClienteUser> clientesOnline = response.getOnlineUsers();
    Afiliado afiliado = UsuarioUtil.getAfiliado(request);
    if (!Objects.isNull(clientesOnline)) {
      for (int i = clientesOnline.size() - 1; i >= 0; i--) {
        ClienteUser user = clientesOnline.get(i);
        if (Objects.isNull(afiliado) || !afiliado.getId().equals(user.getIdAfiliado())) {
          clientesOnline.remove(user);
        }
      }
    }

    // Realiza a filtragem dos Experts que pertencem ao afiliado.
    return response;
  }

  public void fecharPosicao(String conta, String ativo) {
    long inicio = System.currentTimeMillis();
    restTemplate.getForObject(Constantes.ROUTER_CLOSE_POSITION_URL + ativo + "/" + conta,
        String.class);
    System.out.println(DataConverter.nowBr() + " [RouterService] Fechando posição do " + ativo
        + " na conta " + conta + " em " + (System.currentTimeMillis() - inicio) + " ms");
  }

  public List<AccountInfoMeta> getAccountInfo() {
    AccountInfosMT response =
        restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_INFO_URL, AccountInfosMT.class);
    if (response != null && response.getInfos() != null) {
      Collections.sort(response.getInfos(), new Comparator<AccountInfoMeta>() {
        @Override
        public int compare(AccountInfoMeta o1, AccountInfoMeta o2) {
          if (o1 == null && o2 != null)
            return -1;
          if (o1 != null && o2 == null)
            return 1;
          if (o1 == null && o2 == null)
            return 0;
          if (o1.getBalance() == null && o2.getBalance() != null)
            return -1;
          if (o1.getBalance() == null && o2.getBalance() == null)
            return 0;
          return o1.getBalance().compareTo(o2.getBalance());
        }
      });
      return response.getInfos();
    }
    return new ArrayList<>();

  }

  /**
   * Notifica o router da pausa da conta indicada.
   * 
   * @param conta Conta.
   */
  public void pause(String conta) {
    restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_PAUSE_URL + "/" + conta, String.class);
  }

  /**
   * Notifica o router da continuação (unpause) da conta indicada.
   * 
   * @param conta Conta.
   */
  public void resume(String conta) {
    restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_RESUME_URL + "/" + conta, String.class);
  }

  public void sendMarketOrder(String conta, String ativo, BigDecimal volume,
      DirecaoOperacaoEnum direcao) {
    restTemplate.getForObject(Constantes.ROUTER_SEND_MARKET_ORDER_URL + conta + "/" + ativo + "/"
        + volume + "/" + direcao, String.class);
  }

  /**
   * Envia a ordem a mercado.
   * 
   * @param marketOrder Ordem.
   */
  public void sendMarketOrder(MarketOrderRequest marketOrder) {
    restTemplate.postForObject(URI.create("http://router.versatil-ia.com.br:8066/order/market"),
        marketOrder, String.class);
  }

  public AtivoCandlesResponse getCandles(CandlesRequest request) {
    AtivoCandlesResponse response = restTemplate.postForObject(
        URI.create(Constantes.ROUTER_CANDLES_REQUEST_URL), request, AtivoCandlesResponse.class);
    return response;
  }

  /**
   * Retorna o Status de conexão de todos os afiliados.
   * 
   * @return Status de conexão.
   */
  public EndpointStatusResponse getStatusGlobal() {
    EndpointStatusResponse response =
        restTemplate.getForObject(Constantes.ROUTER_STATUS_URL, EndpointStatusResponse.class);
    return response;
  }

  public ExpertStatusResponse getExpertStatus() {
    ExpertStatusResponse response =
        restTemplate.getForObject(Constantes.ROUTER_EXPERT_STATUS_URL, ExpertStatusResponse.class);
    return response;
  }
}
