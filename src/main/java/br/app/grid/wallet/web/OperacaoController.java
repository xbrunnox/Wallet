package br.app.grid.wallet.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeResumoContaResponse;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.converter.OperacaoResponseConverter;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;
import br.app.grid.wallet.web.response.OperacaoResponse;

@RestController
@RequestMapping("/operacao")
public class OperacaoController {

	@Autowired
	private OperacaoService service;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private EndpointService endpointService;

	@PostMapping("/registrar")
	public Operacao registrar(@RequestBody GravarOperacaoRequest request) {
		return service.save(request);
	}

	@GetMapping("/listar")
	public List<OperacaoResponse> listar() {
		return OperacaoResponseConverter.converter(service.getList());
	}

	@GetMapping("/{id}")
	public OperacaoResponse get(@PathVariable(name = "id") int id) {
		return OperacaoResponseConverter.converter(service.get(id));
	}

	@GetMapping("/andamento")
	public ModelAndView emAndamento() {
		if (!UsuarioUtil.isLogged(request)) {
			return new ModelAndView("redirect:/login");
		}
		Map<String, String> mapaStatus = new HashMap<>();
		EndpointStatusResponse status = endpointService.getStatus();
		for (ClienteUser user : status.getOnlineUsers()) {
			mapaStatus.put(user.getLicenca(), user.getLicenca());
		}
		List<Operacao> operacoes = service.getList();
		List<OperacaoResponse> operacoesResponse = OperacaoResponseConverter.converter(operacoes);
		for (OperacaoResponse operacao : operacoesResponse) {
			operacao.setOnline(mapaStatus.containsKey(operacao.getConta()));
		}
		LocalDate data = LocalDate.now();
		List<Trade> trades = tradeService.getList(data);
		Map<String, TradeResumoContaResponse> mapaContaResponse = new HashMap<>();
		List<TradeResumoContaResponse> resumos = new ArrayList<>();
		Double total = 0.0;
		for (Trade trade : trades) {
			total += trade.getResultado();
			TradeResumoContaResponse resumo = mapaContaResponse.get(trade.getConta().getId());
			if (resumo == null) {
				resumo = TradeResumoContaResponse.builder().conta(trade.getConta().getId()).corretora(trade.getConta().getCorretora())
						.nome(trade.getConta().getNome()).operacoes(0).total(BigDecimal.ZERO).build();
				mapaContaResponse.put(trade.getConta().getId(), resumo);
				resumos.add(resumo);
			}
			resumo.setOperacoes(resumo.getOperacoes() + 1);
			resumo.setTotal(resumo.getTotal().add(BigDecimal.valueOf(trade.getResultado())));
		}
		Collections.sort(resumos, new Comparator<TradeResumoContaResponse>() {

			@Override
			public int compare(TradeResumoContaResponse o1, TradeResumoContaResponse o2) {
				int resultado = o2.getTotal().compareTo(o1.getTotal());
				if (resultado != 0)
					return resultado;
				return o1.getNome().compareTo(o2.getNome());
			}

		});
		ModelAndView view = new ModelAndView("operacao/andamento");
		view.addObject("operacoesList", operacoesResponse);
		view.addObject("tradesList", trades);
		view.addObject("total", total);
		view.addObject("resumosList", resumos);
		view.addObject("data", data);
		return view;
	}

}
