package br.app.grid.wallet.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.dividendo.Dividendo;
import br.app.grid.wallet.dividendo.DividendoService;
import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.grafico.SerieInt;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.log.LogComando;
import br.app.grid.wallet.log.LogComandoService;
import br.app.grid.wallet.log.LogOnline;
import br.app.grid.wallet.log.LogOnlineService;
import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.FiiUtil;
import br.app.grid.wallet.util.converter.ContaResultadoAssinaturaResponseConverter;
import br.app.grid.wallet.web.response.ContaResultadoAssinaturaResponse;

@RestController
@RequestMapping("")
public class IndexController {

	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private DividendoService dividendoService;

	@Autowired
	private ContaService contaService;

	@Autowired
	private EndpointService endpointService;

	@Autowired
	private LogComandoService logComandoService;

	@Autowired
	private LogOnlineService logOnlineService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/dividendo/{ativo}")
	public @ResponseBody FiiDividendo ultimo(@PathVariable("ativo") String ativo) {
		Dividendo dividendo = dividendoService.getDividendo(ativo);
		if (dividendo == null)
			return null;
		return new FiiDividendo(dividendo);
	}

	@GetMapping("/dividendoDebug/{ativo}")
	public @ResponseBody FiiDividendo ultimoDebug(@PathVariable("ativo") String ativo) {
		List<FiiDividendo> historico = FiiUtil.getHistorico(ativo.toLowerCase());
		if (historico == null || historico.size() == 0)
			return null;
		return historico.get(0);
	}

	@GetMapping("/dividendos/{ativo}")
	public @ResponseBody List<FiiDividendo> historico(@PathVariable("ativo") String ativo) {
		return FiiUtil.getHistorico(ativo.toLowerCase());
	}

	@GetMapping("/delta")
	public ModelAndView delta() {
		ModelAndView view = new ModelAndView("index/delta");
		return view;
	}

	@GetMapping("/contas")
	public ModelAndView contas() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<ContaResultadoAssinaturaResponse> contas = ContaResultadoAssinaturaResponseConverter
				.converter(contaService.getListContaResultado());
		List<Assinatura> assinaturas = assinaturaService.getList();
		Map<String, LocalDate> mapaVencimentos = new HashMap<>();
		for (Assinatura assinatura : assinaturas) {
			LocalDate data = mapaVencimentos.get(assinatura.getConta().getId());
			if (data == null)
				mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
			else if (data.compareTo(assinatura.getDataVencimento()) < 0)
				mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
		}
		Double total = 0.0;
		for (ContaResultadoAssinaturaResponse conta : contas) {
			if (conta.getResultado() != null)
				total += conta.getResultado();
			conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
		}
		ModelAndView view = new ModelAndView("index/contas");
		view.addObject("contasList", contas);
		view.addObject("contas", contas.size());
		view.addObject("total", total);
		return view;
	}

	@GetMapping("/resultados")
	public ModelAndView resultados() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<ContaResultadoAssinaturaResponse> contas = ContaResultadoAssinaturaResponseConverter
				.converter(contaService.getListContaResultado());
		List<Assinatura> assinaturas = assinaturaService.getList();
		Map<String, LocalDate> mapaVencimentos = new HashMap<>();
		for (Assinatura assinatura : assinaturas) {
			LocalDate data = mapaVencimentos.get(assinatura.getConta().getId());
			if (data == null)
				mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
			else if (data.compareTo(assinatura.getDataVencimento()) < 0)
				mapaVencimentos.put(assinatura.getConta().getId(), assinatura.getDataVencimento());
		}
		Double total = 0.0;
		for (int i = contas.size() - 1; i >= 0; i--) {
			ContaResultadoAssinaturaResponse conta = contas.get(i);
			if (conta.getResultado() == null || conta.getResultado() == 0.0) {
				contas.remove(i);
			} else {
				total += conta.getResultado();
				conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
			}
		}
		ModelAndView view = new ModelAndView("index/resultados");
		view.addObject("contasList", contas);
		view.addObject("contas", contas.size());
		view.addObject("total", total);
		return view;
	}

	@GetMapping("/pendencias")
	public ModelAndView pendencias() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<AssinaturaPendenciaView> pendencias = assinaturaService.getPendencias();
		ModelAndView view = new ModelAndView("index/pendencias");
		view.addObject("pendenciasList", pendencias);
		return view;
	}

	/**
	 * Exibe as assinatura ativas.
	 * 
	 * @return
	 */
	@GetMapping("/ativas")
	public ModelAndView ativas() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<AssinaturaAtivaView> assinaturasAtivas = assinaturaService.getAtivas();
		ModelAndView view = new ModelAndView("assinatura/ativas");
		view.addObject("ativasList", assinaturasAtivas);
		return view;
	}

	@GetMapping
	public ModelAndView index() {
		if (!UsuarioUtil.isLogged(request)) {
			return new ModelAndView("redirect:/login");
		}
		ModelAndView view = new ModelAndView("index/index");
		return view;
	}

	@GetMapping("/inativas")
	public ModelAndView inativas() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");

		List<Conta> contasInativas = assinaturaService.getContasInativas();

		ModelAndView view = new ModelAndView("index/inativas");
		view.addObject("contasList", contasInativas);
		view.addObject("contas", contasInativas.size());
		return view;
	}

	@GetMapping("/registrar")
	public ModelAndView registrar() {
		ModelAndView view = new ModelAndView("index/registrar");
		return view;
	}

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("index/login");
		return view;
	}

	@GetMapping("/status")
	public ModelAndView status() {
		ModelAndView view = new ModelAndView("endpoint/status");
		EndpointStatusResponse status = endpointService.getStatus();
		Collections.sort(status.getOnlineUsers(), new Comparator<ClienteUser>() {

			@Override
			public int compare(ClienteUser o1, ClienteUser o2) {
				return o1.getNome().compareTo(o2.getNome());
			}

		});
		view.addObject("status", status);
		return view;
	}

	@GetMapping("/posicoes")
	public ModelAndView posicoes() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		EndpointPositions endpointPosicoes = endpointService.getPosicoes();

		ModelAndView modelAndView = new ModelAndView("index/posicoes");
		modelAndView.addObject("posicoes", endpointPosicoes);
		return modelAndView;
	}

	@GetMapping("/migrar")
	public ModelAndView migrar() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<Conta> contas = contaService.getList();

		ModelAndView modelAndView = new ModelAndView("index/migrar");
		modelAndView.addObject("contasList", contas);
		return modelAndView;
	}

	@GetMapping("/migrar/{contaOrigem}/{contaDestino}")
	public void migrar(@PathVariable("contaOrigem") String contaOrigem,
			@PathVariable("contaDestino") String contaDestino) {
		assinaturaService.migrar(contaOrigem, contaDestino);
	}

	@GetMapping("/monitor")
	public ModelAndView monitor() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		List<LogOnline> logsOnline = logOnlineService.getList24Horas();
		List<SerieInt> series = new ArrayList<>();
		SerieInt usuariosOnline = new SerieInt();
		SerieInt expertsOnline = new SerieInt();
		usuariosOnline.setName("Usuários");
		expertsOnline.setName("Experts");
		for (LogOnline log : logsOnline) {
			usuariosOnline.addData(log.getUsuarios());
			usuariosOnline.addCategoria(formatter.format(log.getHorario()));
			expertsOnline.addData(log.getExperts());
			expertsOnline.addCategoria(formatter.format(log.getHorario()));
		}
		series.add(usuariosOnline);
		series.add(expertsOnline);
		ModelAndView modelAndView = new ModelAndView("index/monitor");
		modelAndView.addObject("chartData", series);
		return modelAndView;
	}

	@GetMapping("/logs")
	public ModelAndView logs() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<LogComando> logs = logComandoService.getListDia();
		ModelAndView modelAndView = new ModelAndView("log/logs");
		modelAndView.addObject("logsList", logs);
		return modelAndView;

	}
}
