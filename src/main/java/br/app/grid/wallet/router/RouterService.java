package br.app.grid.wallet.router;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.commons.AccountInfoMeta;
import br.app.grid.wallet.enums.DirecaoOperacaoEnum;
import br.app.grid.wallet.meta.AccountInfosMT;
import br.app.grid.wallet.meta.ContaHistoricoMT;
import br.app.grid.wallet.meta.ContaPosicoesMT;
import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.meta.PosicaoMT;
import br.app.grid.wallet.socket.DataConverter;
import br.app.grid.wallet.util.Constantes;

@Service
public class RouterService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AssinaturaService assinaturaService;

	public ContaHistoricoMT getHistoricoMetatrader(String conta, LocalDate dataInicial, LocalDate dataFinal) {
		Assinatura assinatura = assinaturaService.getList(conta).get(0);
		ContaHistoricoMT response = restTemplate.getForObject("http://" + assinatura.getServidor().getHostname()
				+ ":8080/router/historico/" + conta + "/" + dataInicial + "/" + dataFinal, ContaHistoricoMT.class);
		return response;
	}

	public EndpointPositions getPosicoes() {
		EndpointPositions response = restTemplate.getForObject(Constantes.ROUTER_POSITIONS_URL,
				EndpointPositions.class);
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

	public EndpointStatusResponse getStatus() {
		EndpointStatusResponse response = restTemplate.getForObject(Constantes.ROUTER_STATUS_URL,
				EndpointStatusResponse.class);
		return response;
	}

	public void fecharPosicao(String conta, String ativo) {
		long inicio = System.currentTimeMillis();
		restTemplate.getForObject(Constantes.ROUTER_CLOSE_POSITION_URL + ativo + "/" + conta, String.class);
		System.out.println(DataConverter.nowBr() + " [RouterService] Fechando posição do " + ativo + " na conta "
				+ conta + " em " + (System.currentTimeMillis() - inicio) + " ms");
	}

	public void sendMarketOrder(String conta, String ativo, BigDecimal volume, DirecaoOperacaoEnum direcao) {
		restTemplate.getForObject(
				Constantes.ROUTER_SEND_MARKET_ORDER_URL + conta + "/" + ativo + "/" + volume + "/" + direcao,
				String.class);
	}

	public List<AccountInfoMeta> getAccountInfo() {
		AccountInfosMT response = restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_INFO_URL, AccountInfosMT.class);
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
	 * @param conta Conta.
	 */
	public void pause(String conta) {
		restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_PAUSE_URL + "/" + conta, String.class);
	}
	
	/**
	 * Notifica o router da continuação (unpause) da conta indicada. 
	 * @param conta Conta.
	 */
	public void resume(String conta) {
		restTemplate.getForObject(Constantes.ROUTER_ACCOUNT_RESUME_URL + "/" + conta, String.class);
	}
}
