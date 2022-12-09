package br.app.grid.wallet.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.meta.PosicoesMT;
import br.app.grid.wallet.socket.DataConverter;

@Service
public class EndpointService {

	@Autowired
	private RestTemplate restTemplate;

	public EndpointStatusResponse getStatus() {
		long inicio = System.currentTimeMillis();
		EndpointStatusResponse response = restTemplate.getForObject("http://srv1.versatil-ia.com.br:8080/router/status",
				EndpointStatusResponse.class);
		System.out.println(DataConverter.nowBr() + " [EndpointService] Consultando Status em "
				+ (System.currentTimeMillis() - inicio) + " ms");
		return response;
	}

	public PosicoesMT getPosicoes(String conta) {
		long inicio = System.currentTimeMillis();
		PosicoesMT response = restTemplate.getForObject("http://srv1.versatil-ia.com.br:8080/router/posicoes/" + conta,
				PosicoesMT.class);
		System.out.println(DataConverter.nowBr() + " [EndpointService] Consultando posições [" + conta + "] em "
				+ (System.currentTimeMillis() - inicio) + " ms");
		return response;
	}

	public EndpointPositions getPosicoes() {
		long inicio = System.currentTimeMillis();
		EndpointPositions response = restTemplate.getForObject("http://srv1.versatil-ia.com.br:8080/router/posicoes",
				EndpointPositions.class);
		System.out.println(DataConverter.nowBr() + " [EndpointService] Consultando posições em "
				+ (System.currentTimeMillis() - inicio) + " ms");
		return response;
	}

	public void fecharPosicao(String conta, String ativo) {
		long inicio = System.currentTimeMillis();
		restTemplate.getForObject("http://srv1.versatil-ia.com.br:8080/position/close/" + conta + "/" + ativo,
				String.class);
		System.out.println(DataConverter.nowBr() + " [EndpointService] Fechando posição do " + ativo + " na conta "
				+ conta + " em " + (System.currentTimeMillis() - inicio) + " ms");
	}

}
