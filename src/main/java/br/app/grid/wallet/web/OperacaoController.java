package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;

@RestController
@RequestMapping("/operacao")
public class OperacaoController {
	
	@Autowired
	private OperacaoService service;
	
	@PostMapping("/registrar")
	public Operacao registrar(@RequestBody GravarOperacaoRequest request) {
		
		Operacao operacao = Operacao.builder()
				.ativo(request.getAtivo())
				.dataDeEntrada(request.getDataDeEntrada())
				.dataDeSaida(request.getDataDeSaida())
				.entrada(request.getEntrada())
				.licenca(request.getLicenca())
				.pontos(request.getPontos())
				.saida(request.getSaida())
				.valor(request.getValor())
				.volume(request.getVolume())
				.direcao(request.getDirecao())
				.build();
		
		
		return service.save(operacao);
	}

}
