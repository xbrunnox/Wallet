package br.app.grid.wallet.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;
import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.ativo.AtivoService;
import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.service.BacktestService;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoResultadoMesVO;
import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.client.ClienteUser;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.dashboard.DashboardService;
import br.app.grid.wallet.dividendo.Dividendo;
import br.app.grid.wallet.dividendo.DividendoService;
import br.app.grid.wallet.enums.TimeFrameEnum;
import br.app.grid.wallet.estrategia.EstrategiaFechamentoItem;
import br.app.grid.wallet.estrategia.service.EstrategiaAberturaFechamentoService;
import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.grafico.SerieInt;
import br.app.grid.wallet.homebroker.HomeBroker;
import br.app.grid.wallet.homebroker.HomeBrokerService;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.log.LogComando;
import br.app.grid.wallet.log.LogComandoService;
import br.app.grid.wallet.log.LogOnline;
import br.app.grid.wallet.log.LogOnlineService;
import br.app.grid.wallet.meta.ContaPosicoesMT;
import br.app.grid.wallet.meta.EndpointPositions;
import br.app.grid.wallet.meta.PosicaoMT;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.resultado.ResultadoService;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.router.RouterService;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.FiiUtil;
import br.app.grid.wallet.util.converter.CandleConverter;
import br.app.grid.wallet.util.converter.ContaResultadoAssinaturaResponseConverter;
import br.app.grid.wallet.web.request.CandlesRequest;
import br.app.grid.wallet.web.response.AtivoCandlesResponse;
import br.app.grid.wallet.web.response.CandleResponse;
import br.app.grid.wallet.web.response.ContaResultadoAssinaturaResponse;
import br.app.grid.wallet.web.response.PosicaoGroupResponse;
import br.app.grid.wallet.web.response.PosicaoResponse;

@RestController
@RequestMapping("")
public class IndexController {

	@Autowired
	private AtivoService ativoService;

	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private BacktestService backtestService;

	@Autowired
	private CandleService candleService;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private DividendoService dividendoService;

	@Autowired
	private ContaService contaService;
	
	@Autowired
	private EstrategiaAberturaFechamentoService estrategiaAberturaFechamentoService;

	@Autowired
	private RouterService routerService;

	@Autowired
	private LogComandoService logComandoService;

	@Autowired
	private LogOnlineService logOnlineService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private RoboService automacaoService;

	@Autowired
	private HomeBrokerService homeBrokerService;

	@Autowired
	private ResultadoService resultadoService;

	@GetMapping("/acompanhamento/{expert}")
	public @ResponseBody ModelAndView acompanhamento(@PathVariable("expert") String expert) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		ModelAndView modelAndView = new ModelAndView("acompanhamento/expert");
		modelAndView.addObject("expert", expert);
		return modelAndView;
	}

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
				total += conta.getResultado().doubleValue();
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
			if (conta.getResultado() == null || conta.getResultado().compareTo(BigDecimal.ZERO) == 0) {
				contas.remove(i);
			} else {
				total += conta.getResultado().doubleValue();
				conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
			}
		}
		ModelAndView view = new ModelAndView("index/resultados");
		view.addObject("contasList", contas);
		view.addObject("contas", contas.size());
		view.addObject("total", total);
		return view;
	}

	@GetMapping("/resultados/{expert}")
	public ModelAndView resultados(@PathVariable(name = "expert") String expert) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<ContaResultadoAssinaturaResponse> contas = resultadoService.getResultado(expert);
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
			if (conta.getResultado() == null || conta.getResultado().compareTo(BigDecimal.ZERO) == 0) {
				contas.remove(i);
			} else {
				total += conta.getResultado().doubleValue();
				conta.setDataDeVencimento(mapaVencimentos.get(conta.getId()));
			}
		}
		ModelAndView view = new ModelAndView("index/resultados");
		view.addObject("automacao", expert);
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
		ModelAndView view = new ModelAndView("assinatura/pendencias");
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
		EndpointStatusResponse status = routerService.getStatus();
		Collections.sort(status.getOnlineUsers(), new Comparator<ClienteUser>() {

			@Override
			public int compare(ClienteUser o1, ClienteUser o2) {
				if (o1.getPausado() && !o2.getPausado())
					return -1;
				if (!o1.getPausado() && o2.getPausado())
					return 1;
				return StringUtils.stripAccents(o1.getNome()).compareTo(StringUtils.stripAccents(o2.getNome()));
			}

		});
		view.addObject("status", status);
		return view;
	}

	@GetMapping("/posicoes")
	public ModelAndView posicoes() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<PosicaoResponse> posicoes = new ArrayList<>();
		Map<String, PosicaoGroupResponse> mapaPosicaoGrupo = new HashMap<>();

		EndpointPositions endpointPosicoes = routerService.getPosicoes();
		if (endpointPosicoes != null && endpointPosicoes.getContas() != null) {
			for (ContaPosicoesMT conta : endpointPosicoes.getContas()) {
				if (conta != null && conta.getPosicoes() != null) {
					for (PosicaoMT posicao : conta.getPosicoes()) {
						if (posicao != null) {
							String posicaoTitulo = posicao.getAtivo() + " - " + posicao.getVolume()
									+ posicao.getDirecao();
							PosicaoGroupResponse grupo = mapaPosicaoGrupo.get(posicaoTitulo);
							if (grupo == null) {
								grupo = PosicaoGroupResponse.builder().ativo(posicao.getAtivo())
										.direcao(posicao.getDirecao()).posicoes(new ArrayList<>()).titulo(posicaoTitulo)
										.volume(posicao.getVolume()).build();
								mapaPosicaoGrupo.put(posicaoTitulo, grupo);
							}
							PosicaoResponse posicaoResponse = PosicaoResponse.builder().abertura(posicao.getAbertura())
									.ativo(posicao.getAtivo()).conta(conta.getConta()).data(posicao.getData())
									.direcao(posicao.getDirecao()).expert(posicao.getExpert()).nome(conta.getNome())
									.profit(posicao.getProfit()).volume(posicao.getVolume()).servidor(conta.getServer())
									.corretora(conta.getCorretora()).build();
							posicoes.add(posicaoResponse);
							grupo.add(posicaoResponse);
						}
					}
				}
			}
			Collections.sort(posicoes, new Comparator<PosicaoResponse>() {
				@Override
				public int compare(PosicaoResponse o1, PosicaoResponse o2) {
					int resultado = o1.getAtivo().compareTo(o2.getAtivo());
					if (resultado != 0)
						return resultado;
					resultado = o1.getDirecao().compareTo(o2.getDirecao());
					if (resultado != 0)
						return resultado;
					resultado = o1.getVolume().compareTo(o2.getVolume());
					if (resultado != 0)
						return resultado;
					return 0;
				}
			});
		}

		List<PosicaoGroupResponse> retornoPosicoesGrupo = new ArrayList<>();
		for (String titulo : mapaPosicaoGrupo.keySet()) {
			retornoPosicoesGrupo.add(mapaPosicaoGrupo.get(titulo));
		}

		Collections.sort(retornoPosicoesGrupo, new Comparator<PosicaoGroupResponse>() {

			@Override
			public int compare(PosicaoGroupResponse o1, PosicaoGroupResponse o2) {
				int resultado = o1.getPosicoes().size() - o2.getPosicoes().size();
				if (resultado != 0)
					return resultado;
				return o1.getAtivo().compareTo(o2.getAtivo());
			}

		});

		ModelAndView modelAndView = new ModelAndView("index/posicoes");
		modelAndView.addObject("posicoes", posicoes);
		modelAndView.addObject("posicoesGrupos", retornoPosicoesGrupo);
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

	@GetMapping("/detalhes/{conta}")
	public ModelAndView detalhes(@PathVariable(name = "conta") String idConta) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		BigDecimal total = BigDecimal.ZERO;
		List<Trade> trades = tradeService.getList(idConta);
		for (Trade trade : trades) {
			total = total.add(BigDecimal.valueOf(trade.getResultado()));
		}
		Conta conta = contaService.get(idConta);

		List<Assinatura> assinaturas = assinaturaService.getList(idConta);
		List<Assinatura> subcontas = new ArrayList<>();
		Assinatura assinatura = null;
		if (assinaturas.size() > 0) {
			assinatura = assinaturas.get(0);
			subcontas = assinaturaService.getListSubContas(assinatura.getId());
		}

		List<AssinaturaPagamento> pagamentos = assinaturaService.getListPagamentos(idConta);
		List<AssinaturaExpert> experts = assinaturaService.getExperts(assinatura);

		ModelAndView view = new ModelAndView("conta/detalhes");
		view.addObject("assinatura", assinatura);
		view.addObject("subContaList", subcontas);
		view.addObject("tradesList", trades);
		view.addObject("pagamentosList", pagamentos);
		view.addObject("expertList", experts);
		view.addObject("total", total);
		view.addObject("conta", conta);
		return view;

	}

	@GetMapping("/terminal")
	public ModelAndView terminal() {
		ModelAndView view = new ModelAndView("index/terminal");
		return view;
	}

	@GetMapping("/assinaturas")
	public ModelAndView assinaturas() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		ModelAndView view = new ModelAndView("menu/assinaturas");
		return view;
	}

	@GetMapping("/pagamento-tratamento-pendente")
	public ModelAndView pagamentoTratamentoPendente() {
		if (!UsuarioUtil.isLogged(request)) {
			return new ModelAndView("redirect:/login");
		}
		List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();
		Collections.sort(pagamentos, new Comparator<Pagamento>() {
			@Override
			public int compare(Pagamento p1, Pagamento p2) {
				return p2.getDataAtualizacao().compareTo(p1.getDataAtualizacao());
			}
		});
		ModelAndView view = new ModelAndView("pagamento/tratamento-pendente");
		view.addObject("pagamentosList", pagamentos);
		return view;
	}

	/**
	 * Realiza a exibição do Home Broker.
	 * 
	 * @return View do Home Broker.
	 */
	@GetMapping("/home-broker")
	public ModelAndView homebroker() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<Robo> automacoes = automacaoService.getListEnabled();
		List<HomeBroker> homeBrokers = homeBrokerService.getList();
		ModelAndView view = new ModelAndView("index/home-broker");
		view.addObject("automacaoList", automacoes);
		view.addObject("homeBrokerList", homeBrokers);
		return view;
	}

	/**
	 * Realiza a exibição dos backtests catalogados.
	 * 
	 * @return View dos backtests.
	 */
	@GetMapping("/backtests")
	public ModelAndView backtests() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		List<Backtest> backtests = backtestService.getList();
		ModelAndView view = new ModelAndView("index/backtests");
		view.addObject("backtestList", backtests);
		return view;
	}

	/**
	 * Realiza a exibição do backtest indicado.
	 * 
	 * @param id ID.
	 * @return Resumo do backtest por dia.
	 */
	@GetMapping("/backtest/{idBacktest}")
	public ModelAndView backtestGet(@PathVariable(name = "idBacktest") Integer idBacktest) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		Backtest backtest = backtestService.get(idBacktest);
		List<BacktestOperacaoResultadoMesVO> resultadosDias = backtestService.getListResultadosMensais(idBacktest);
		ModelAndView view = new ModelAndView("backtest/backtest");
		view.addObject("resultadoList", resultadosDias);
		view.addObject("backtest", backtest);
		return view;
	}

	/**
	 * Realiza a exibição do dashboard.
	 * 
	 * @param id ID.
	 * @return Exibição do dashboard.
	 */
	@GetMapping("/dashboard")
	public ModelAndView dashboard() {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");

		ModelAndView view = new ModelAndView("index/dashboard");
		view.addObject("dashboardDelta", dashboardService.getDashboardDelta());
		return view;
	}

	@GetMapping("/analisar")
	public void analisar() {

		LocalDate dataInicial = LocalDate.of(2023, 1, 1);
		LocalDate dataFinal = LocalDate.of(2023, 8, 22);
		List<Ativo> ativos = ativoService.getList();

		List<EstrategiaFechamentoItem> itens = new ArrayList<>();
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		for (Ativo ativo : ativos) {
			long horaDeInnicio = System.currentTimeMillis();
			if (ativo.getCategoria().getId().equals(Integer.parseInt("3"))) {
				System.out.println("Analisando: " + ativo.getCodigo());
				List<Candle> candles = candleService.getList(ativo.getCodigo(), TimeFrameEnum.D1, dataInicial,
						dataFinal);
				if (candles.size() == 0) {
					candles = new ArrayList<>();

				}

				BigDecimal inicio = BigDecimal.valueOf(0.005);
				BigDecimal fim = BigDecimal.valueOf(0.050);
				BigDecimal incremento = BigDecimal.valueOf(0.001);
				BigDecimal passo = inicio;
				System.out.println("Candles encontratos: " + candles.size());
				BigDecimal volumeMedio = BigDecimal.ZERO;
				boolean achou = candles.size() > 0;
				for (Candle candle : candles) {
					volumeMedio = volumeMedio
							.add(candle.getAbertura().multiply(BigDecimal.valueOf(candle.getVolume())));
				}
				if (!achou) {
					CandlesRequest candleRequest = CandlesRequest.builder().ativo(ativo.getCodigo())
							.dataFinal(dataFinal).dataInicial(dataInicial).timeFrame(16408).build();
					AtivoCandlesResponse response = routerService.getCandles(candleRequest);
					for (CandleResponse candle : response.getCandles()) {
						Candle cand = CandleConverter.convert(response, candle);
						candles.add(cand);
						try {
							candleService.gravar(cand);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}

				}
				if (candles.size() != 0) {
					volumeMedio = volumeMedio.divide(BigDecimal.valueOf(candles.size()), 2, RoundingMode.HALF_DOWN);
				}

				while (passo.compareTo(fim) <= 0 && candles.size() > 0) {
					int acertos = 0;
					int erros = 0;
					List<BigDecimal> resultados = new ArrayList<>();
					Candle candleAnterior = null;
					BigDecimal percentualGanho = BigDecimal.ZERO;

					for (Candle candle : candles) {
						if (candleAnterior != null) {
							BigDecimal gatilho = candleAnterior.getFechamento()
									.multiply(BigDecimal.valueOf(1).subtract(passo));

							if (candle.getAbertura().compareTo(gatilho) < 0
									|| candle.getMinima().compareTo(gatilho) < 0) {
								// Houve entrada
								BigDecimal entrada = (candle.getAbertura().compareTo(gatilho) < 0 ? candle.getAbertura()
										: gatilho);
								BigDecimal stop = entrada.multiply(BigDecimal.valueOf(0.95));
								BigDecimal saida = candle.getFechamento();
								if (candle.getMinima().compareTo(stop) < 0) {
									saida = stop;
								}
								BigDecimal lucro = saida.subtract(entrada);
								resultados.add(lucro);
								percentualGanho = percentualGanho.add(saida.divide(entrada, 2, RoundingMode.HALF_DOWN)
										.subtract(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(100)));
								if (lucro.compareTo(BigDecimal.ZERO) >= 0) {
									acertos++;
								} else {
									erros++;
								}
							}
						}
						candleAnterior = candle;
					}
//			System.out.println("Resultados: " + passo.multiply(BigDecimal.valueOf(100)) + "%");
					BigDecimal total = BigDecimal.ZERO;
					for (BigDecimal resultado : resultados) {
						total = total.add(resultado);
					}
					BigDecimal percentual = BigDecimal.ZERO;
					if ((acertos + erros) > 0) {
						BigDecimal totalOperacoes = BigDecimal.valueOf(acertos).add(BigDecimal.valueOf(erros));
						percentual = BigDecimal.valueOf(acertos).multiply(BigDecimal.valueOf(100))
								.divide(totalOperacoes, 0, RoundingMode.HALF_UP);
					}
					System.out.println(ativo.getCodigo() + "\t" + ativo.getNome() + "\t"
							+ passo.multiply(BigDecimal.valueOf(100)) + "%\t" + (acertos + erros) + "\t" + acertos
							+ "\t" + erros + "\t" + formatter.format(percentual) + "%\t" + formatter.format(total)
							+ "\t" + percentualGanho + "%" + "\t" + formatter.format(volumeMedio));
					itens.add(EstrategiaFechamentoItem.builder().acertos(acertos).codigo(ativo.getCodigo())
							.ativo(ativo.getNome()).erros(erros).gatilho(passo.multiply(BigDecimal.valueOf(100)))
							.resultado(total).resultadoPercentual(percentualGanho).volumeNegociado(volumeMedio)
							.build());
					passo = passo.add(incremento);
				}
			}
			System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - horaDeInnicio) + " ms");
		}
		// Criando excel

		// Criando o arquivo e uma planilha chamada "Product"
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Product");

		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short) 400);

		int rownum = 0;
		int cellnum = 0;
		Cell cell;
		Row row;

		// Configurando estilos de células (Cores, alinhamento, formatação, etc..)
		HSSFDataFormat numberFormat = workbook.createDataFormat();

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setAlignment(CellStyle.ALIGN_CENTER);
		textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
		numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// Configurando Header
		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Código");

		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Ativo");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Gatilho %");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Operações");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Acertos");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Erros");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("% Acerto");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Resultado");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("% Resultado");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Volume Financ.");

		// Adicionando os dados dos produtos na planilha
		for (EstrategiaFechamentoItem item : itens) {
			row = sheet.createRow(rownum++);
			cellnum = 0;

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getCodigo());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getAtivo());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getGatilho().doubleValue());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getAcertos() + item.getErros());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getAcertos());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getErros());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			BigDecimal percentual = BigDecimal.ZERO;
			if (item.getAcertos() + item.getErros() > 0)
				percentual = BigDecimal.valueOf(item.getAcertos()).multiply(BigDecimal.valueOf(100))
						.divide(BigDecimal.valueOf(item.getAcertos() + item.getErros()), 0, RoundingMode.HALF_UP);
			cell.setCellValue(percentual.doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getResultado().doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getResultadoPercentual().doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getVolumeNegociado().doubleValue());
		}

		try {

			// Escrevendo o arquivo em disco
			FileOutputStream out = new FileOutputStream(new File("estatisticas.xls"));
			workbook.write(out);
			out.close();
//		workbook.close();
			System.out.println("Success!!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/analisar-sem-stop")
	public void analisarSemStop() {

		LocalDate dataInicial = LocalDate.of(2023, 1, 1);
		LocalDate dataFinal = LocalDate.of(2023, 8, 22);
		List<Ativo> ativos = ativoService.getList();

		List<EstrategiaFechamentoItem> itens = new ArrayList<>();
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		for (Ativo ativo : ativos) {
			List<EstrategiaFechamentoItem> itensDoAtivo = new ArrayList<>();
			long horaDeInnicio = System.currentTimeMillis();
			if (ativo.getCategoria().getId().equals(Integer.parseInt("3"))) {
				System.out.println("Analisando: " + ativo.getCodigo());
				List<Candle> candles = candleService.getList(ativo.getCodigo(), TimeFrameEnum.D1, dataInicial,
						dataFinal);
				if (candles.size() == 0) {
					candles = new ArrayList<>();

				}

				BigDecimal inicio = BigDecimal.valueOf(0.005);
				BigDecimal fim = BigDecimal.valueOf(0.050);
				BigDecimal incremento = BigDecimal.valueOf(0.001);
				BigDecimal passo = inicio;
				System.out.println("Candles encontratos: " + candles.size());
				BigDecimal volumeMedio = BigDecimal.ZERO;
				boolean achou = candles.size() > 0;
				for (Candle candle : candles) {
					volumeMedio = volumeMedio
							.add(candle.getAbertura().multiply(BigDecimal.valueOf(candle.getVolume())));
				}
//				if (!achou) {
//					CandlesRequest candleRequest = CandlesRequest.builder().ativo(ativo.getCodigo())
//							.dataFinal(dataFinal).dataInicial(dataInicial).timeFrame(16408).build();
//					AtivoCandlesResponse response = routerService.getCandles(candleRequest);
//					for (CandleResponse candle : response.getCandles()) {
//						Candle cand = CandleConverter.convert(response, candle);
//						candles.add(cand);
//						try {
//							candleService.gravar(cand);
//						} catch (Exception e) {
//							System.out.println(e.getMessage());
//						}
//					}
//
//				}
				if (candles.size() != 0) {
					volumeMedio = volumeMedio.divide(BigDecimal.valueOf(candles.size()), 2, RoundingMode.HALF_DOWN);
				}

				while (passo.compareTo(fim) <= 0 && candles.size() > 0) {
					int acertos = 0;
					int erros = 0;
					List<BigDecimal> resultados = new ArrayList<>();
					Candle candleAnterior = null;
					BigDecimal percentualGanho = BigDecimal.ZERO;

					for (Candle candle : candles) {
						if (candleAnterior != null) {
							BigDecimal gatilho = candleAnterior.getFechamento()
									.multiply(BigDecimal.valueOf(1).subtract(passo));

							if (candle.getAbertura().compareTo(gatilho) < 0
									|| candle.getMinima().compareTo(gatilho) < 0) {
								// Houve entrada
								BigDecimal entrada = (candle.getAbertura().compareTo(gatilho) < 0 ? candle.getAbertura()
										: gatilho);
								BigDecimal stop = entrada.multiply(BigDecimal.valueOf(0.95));
								BigDecimal saida = candle.getFechamento();
//								if (candle.getMinima().compareTo(stop) < 0) {
//									saida = stop;
//								}
								BigDecimal lucro = saida.subtract(entrada);
								resultados.add(lucro);
								percentualGanho = percentualGanho.add(saida.divide(entrada, 2, RoundingMode.HALF_DOWN)
										.subtract(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(100)));
								if (lucro.compareTo(BigDecimal.ZERO) >= 0) {
									acertos++;
								} else {
									erros++;
								}
							}
						}
						candleAnterior = candle;
					}
//			System.out.println("Resultados: " + passo.multiply(BigDecimal.valueOf(100)) + "%");
					BigDecimal total = BigDecimal.ZERO;
					for (BigDecimal resultado : resultados) {
						total = total.add(resultado);
					}
					BigDecimal percentual = BigDecimal.ZERO;
					if ((acertos + erros) > 0) {
						BigDecimal totalOperacoes = BigDecimal.valueOf(acertos).add(BigDecimal.valueOf(erros));
						percentual = BigDecimal.valueOf(acertos).multiply(BigDecimal.valueOf(100))
								.divide(totalOperacoes, 0, RoundingMode.HALF_UP);
					}
					System.out.println(ativo.getCodigo() + "\t" + ativo.getNome() + "\t"
							+ passo.multiply(BigDecimal.valueOf(100)) + "%\t" + (acertos + erros) + "\t" + acertos
							+ "\t" + erros + "\t" + formatter.format(percentual) + "%\t" + formatter.format(total)
							+ "\t" + percentualGanho + "%" + "\t" + formatter.format(volumeMedio));
					itensDoAtivo.add(EstrategiaFechamentoItem.builder().acertos(acertos).codigo(ativo.getCodigo())
							.ativo(ativo.getNome()).erros(erros).gatilho(passo.multiply(BigDecimal.valueOf(100)))
							.resultado(total).resultadoPercentual(percentualGanho).volumeNegociado(volumeMedio)
							.build());
					passo = passo.add(incremento);
				}
			}
			System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - horaDeInnicio) + " ms");
			boolean temGanho = false;
			boolean temLiquidez = false;
			for (EstrategiaFechamentoItem item : itensDoAtivo) {
				if (item.getResultadoPercentual().compareTo(BigDecimal.valueOf(30)) > 0) {
					temGanho = true;
				}
				if (item.getVolumeNegociado().compareTo(BigDecimal.valueOf(800000)) > 0
						|| item.getVolumeNegociado().compareTo(BigDecimal.ZERO) == 0) {
					temLiquidez = true;
				}
			}
			if (temGanho && temLiquidez)
				itens.addAll(itensDoAtivo);
		}
		// Criando excel

		// Criando o arquivo e uma planilha chamada "Product"
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Product");

		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short) 400);

		int rownum = 0;
		int cellnum = 0;
		Cell cell;
		Row row;

		// Configurando estilos de células (Cores, alinhamento, formatação, etc..)
		HSSFDataFormat numberFormat = workbook.createDataFormat();

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setAlignment(CellStyle.ALIGN_CENTER);
		textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
		numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// Configurando Header
		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Código");

//		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Ativo");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Gatilho %");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Operações");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Acertos");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Erros");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("% Acerto");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Resultado");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("% Resultado");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Volume Financ.");

		// Adicionando os dados dos produtos na planilha
		for (EstrategiaFechamentoItem item : itens) {
			row = sheet.createRow(rownum++);
			cellnum = 0;

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getCodigo());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getAtivo());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(item.getGatilho().doubleValue());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getAcertos() + item.getErros());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getAcertos());

			cell = row.createCell(cellnum++);
//			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getErros());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			BigDecimal percentual = BigDecimal.ZERO;
			if (item.getAcertos() + item.getErros() > 0)
				percentual = BigDecimal.valueOf(item.getAcertos()).multiply(BigDecimal.valueOf(100))
						.divide(BigDecimal.valueOf(item.getAcertos() + item.getErros()), 0, RoundingMode.HALF_UP);
			cell.setCellValue(percentual.doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getResultado().doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getResultadoPercentual().doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getVolumeNegociado().doubleValue());
		}

		try {

			// Escrevendo o arquivo em disco
			FileOutputStream out = new FileOutputStream(new File("estatisticas-sem-stop.xls"));
			workbook.write(out);
			out.close();
//		workbook.close();
			System.out.println("Success!!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/atualizar-diarios")
	public void atualizarDiarios() {
		final Integer ACOES = 3;
		LocalDate dataFinal = LocalDate.now().minusDays(1);
		List<Ativo> ativos = ativoService.getListByCategoria(ACOES);
//		ativos = new ArrayList<>();
//		ativos.add(Ativo.builder().nome("Cielo").codigo("CIEL3").build());

		for (Ativo ativo : ativos) {
			LocalDate dataInicial = LocalDate.of(2023, 1, 1);
			long horaDeInicio = System.currentTimeMillis();

			System.out.println("Consultando: " + ativo.getCodigo());
			List<Candle> candles = candleService.getList(ativo.getCodigo(), TimeFrameEnum.D1, dataInicial, dataFinal);

			if (candles.size() > 0) {
				dataInicial = candles.get(candles.size() - 1).getDataHora().toLocalDate().plusDays(1);
			}

			if (dataInicial.compareTo(dataFinal) < 0) {

				CandlesRequest candleRequest = CandlesRequest.builder().ativo(ativo.getCodigo()).dataFinal(dataFinal)
						.dataInicial(dataInicial).timeFrame(16408).build();
				AtivoCandlesResponse response = routerService.getCandles(candleRequest);
				if (response != null && response.getCandles() != null) {
					System.out.println("Candles encontrados: " + response.getCandles().size() + " - "
							+ (System.currentTimeMillis() - horaDeInicio) + " ms");
					for (CandleResponse candle : response.getCandles()) {
						Candle cand = CandleConverter.convert(response, candle);
						candles.add(cand);
						try {
							System.out.println("Gravando " + ativo.getCodigo() + " - " + cand.getDataHora());
							candleService.gravar(cand);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
			System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - horaDeInicio) + " ms");
		}
		System.out.println("Success!!");
	}
	
	@GetMapping("/atualizar-estrategias")
	public String atualizarEstatisticas() {
		estrategiaAberturaFechamentoService.atualizarEstatisticas();
		return "ok";
	}
}