package br.app.grid.wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.ativo.AtivoService;
import br.app.grid.wallet.backtest.service.BacktestOperacaoService;
import br.app.grid.wallet.backtest.service.BacktestService;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.categoria.Categoria;
import br.app.grid.wallet.dashboard.DashboardService;
import br.app.grid.wallet.enums.ServidorTipoEnum;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.servidor.ServidorService;
import br.app.grid.wallet.socket.Router;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private AtivoService ativoService;

	@Autowired
	private ContaService licencaService;

	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private BacktestService backtestService;

	@Autowired
	private BacktestOperacaoService backtestOperacaoService;

	@Autowired
	private ContaService contaService;

	@Autowired
	private RoboService expertService;

	@Autowired
	private OperacaoService operacaoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private ServidorService servidorService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private CandleService candleService;

	@Autowired
	private DashboardService dashboardService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println(LocalDate.now());
//		coletarNoticias();

//		coletarAcoes();

//    LocalDate data = LocalDate.of(2023, 5, 17);
//
//    for (int i = 0; i < 30; i++) {
//      candleService.calcularVwap("WIN@N", 5, data);
//      data = data.minusDays(1);
//    }
//    candleService.calcularVwap("WIN@N", 5, LocalDate.of(2023, 5, 22));

//    dashboardService.getDashboardDelta();

		// Servidor servidor = Servidor.builder()
		// .ativo(true)
		// .hostname("srv1.versatil-ia.com.br")
		// .nome("SRV1")
		// .porta(22631)
		// .tipo(ServidorTipoEnum.ENDPOINT_CUSTOMER)
		// .dataCadastro(LocalDateTime.now())
		// .build();
		// servidorService.gravar(servidor);

		// importarBacktest(2, "indice520560.txt");

		// tratarOperacoes();

		// unificarAssinaturas();

		// ajustarExperts();

		// alocarServidores();

		/*
		 * try { BufferedReader br = new BufferedReader(new FileReader("kiwi.csv"));
		 * String linha = br.readLine(); linha = br.readLine(); DateTimeFormatter
		 * formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); while (linha
		 * != null) { String[] campos = linha.split(","); Pagamento pagamento =
		 * Pagamento.builder() .associado(false) .cpf(campos[5])
		 * .dataAtualizacao(LocalDateTime.parse(campos[27], formatter))
		 * .dataCriacao(LocalDateTime.parse(campos[26], formatter)) .email(campos[4])
		 * .formaDePagamento(null) .idCliente(null) .idLoja("9XismOGxkik60xO")
		 * .idPedido(campos[0]) .idProduto(null) .ip(campos[7]) .nome(campos[3])
		 * .plataforma("Kiwify") .produto(campos[2]) .refPedido(campos[0])
		 * .status(campos[1]) .taxas(Double.valueOf(campos[14])) .telefone(campos[6])
		 * .valor(Double.valueOf(campos[15])) .build(); System.out.println(new
		 * ObjectMapper().writeValueAsString(pagamento)); if
		 * (pagamento.getStatus().equals("paid")) { pagamentoService.gravar(pagamento);
		 * } linha = br.readLine(); } } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		/*
		 * Backtest back = backtestService.get(9);
		 * 
		 * 
		 * // tratarOperacoesAbertas(); List<Trade> trades =
		 * tradeService.getList("GRSXPD"); for (Trade trade : trades) { BigDecimal
		 * entrada = (trade.getDirecao().equals("C") ?
		 * BigDecimal.valueOf(trade.getCompra()) :
		 * BigDecimal.valueOf(trade.getVenda())); BigDecimal saida =
		 * (trade.getDirecao().equals("V") ? BigDecimal.valueOf(trade.getCompra()) :
		 * BigDecimal.valueOf(trade.getVenda()));
		 * 
		 * BacktestOperacao operacao = BacktestOperacao.builder().backtest(back)
		 * .dataEntrada(trade.getDataEntrada()).dataSaida(trade.getDataSaida())
		 * .direcao(trade.getDirecao()).duracao(trade.getDuracao().intValue())
		 * .lucro(BigDecimal.valueOf(trade.getResultado())).precoEntrada(entrada).
		 * precoSaida(saida) .volume(trade.getVolume()).build();
		 * 
		 * backtestOperacaoService.gravar(operacao); }
		 */

	}

	private void coletarAcoes() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("acoes-listadas.csv"));
			String linha = br.readLine();
			Categoria categoria = Categoria.builder().id(3).ganho(1.0).nome("Ação").build();
			while (linha != null) {
				Scanner sc = new Scanner(linha);
				sc.useDelimiter(",");
				String codigo = sc.next();
				String nome = sc.next();
				System.out.println(codigo + " - " + nome);
				sc.close();
				Ativo ativo = Ativo.builder().codigo(codigo).categoria(categoria).nome(nome).build();
				ativoService.salvar(ativo);
				linha = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void coletarNoticias() {
		try {
			URL feedSource = new URL("https://br.investing.com/rss/news_1.rss");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedSource));
			System.out.println(new ObjectMapper().writeValueAsString(feed));
			System.out.println();
			System.out.println();
			System.out.println();
			for (Object entry : feed.getEntries()) {

				if (entry instanceof SyndEntry) {
					SyndEntry entrada = (SyndEntry) entry;
					System.out.println(new ObjectMapper().writeValueAsString(entrada));
					System.out.println(entrada.getLink());
					System.out.println(entrada.getTitleEx().getValue());
				} else {
					System.out.println(entry.getClass().getName());
				}

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void tratarOperacoesAbertas() {
		List<Operacao> operacoes = operacaoService.getList();
		for (Operacao operacao : operacoes) {
			if (operacao.getAtivo().getCodigo().equals("WDOM23")) {
				GravarOperacaoRequest operacaoFechamento = GravarOperacaoRequest.builder()
						.ativo(operacao.getAtivo().getCodigo()).conta(operacao.getConta().getId())
						.data(LocalDateTime.of(2023, 5, 5, 14, 01, 57)).direcao("V")
						.expert(operacao.getExpert().getId()).preco(4961.0).tipo("S").volume(operacao.getVolume())
						.build();
				operacaoService.save(operacaoFechamento);
				System.out.println("Gravando trade");
			}
			if (operacao.getAtivo().getCodigo().equals("WINM23")) {
				GravarOperacaoRequest operacaoFechamento = GravarOperacaoRequest.builder()
						.ativo(operacao.getAtivo().getCodigo()).conta(operacao.getConta().getId())
						.data(LocalDateTime.of(2023, 5, 5, 14, 18, 57)).direcao("C")
						.expert(operacao.getExpert().getId()).preco(105775.0).tipo("S").volume(operacao.getVolume())
						.build();
				operacaoService.save(operacaoFechamento);
				System.out.println("Gravando trade");
			}
		}
	}

	private void alocarServidores() {
		List<Servidor> servidores = servidorService.getList(ServidorTipoEnum.ENDPOINT_CUSTOMER, true);
		List<Assinatura> assinaturas = assinaturaService.getList(LocalDate.now());
		int posicao = 0;
		for (Assinatura assinatura : assinaturas) {
			assinatura.setServidor(servidores.get(posicao));
			assinaturaService.gravar(assinatura);
			posicao++;
			if (posicao >= servidores.size())
				posicao = 0;
			System.out.println("Gravando: " + assinatura.getId());
		}
	}

	private void ajustarExperts() {
		Robo delta = expertService.getById("DELTA");
		List<Assinatura> assinaturas = assinaturaService.getList();
		for (Assinatura assinatura : assinaturas) {
			AssinaturaExpert expertDelta = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(delta)
					.volume(delta.getVolume()).volumeMaximo(delta.getVolume()).build();
			assinaturaService.gravar(expertDelta);
			System.out.println("Gravando " + assinatura.getConta().getId());
		}
	}

	private void unificarAssinaturas() {
		List<Conta> contas = contaService.getList();
		for (Conta conta : contas) {
			List<Assinatura> assinaturas = assinaturaService.getList(conta.getId());
			Collections.sort(assinaturas, new Comparator<Assinatura>() {

				@Override
				public int compare(Assinatura o1, Assinatura o2) {
					if (o1.getId() < o2.getId())
						return -1;
					return 1;
				}
			});
			if (assinaturas.size() > 0) {
				Assinatura assinaturaDefinitiva = assinaturas.get(0);
				String documento = assinaturaDefinitiva.getDocumentoPagamento();
				String email = assinaturaDefinitiva.getEmailPagamento();
				String telefone = assinaturaDefinitiva.getTelefone();
				LocalDate dataDeVencimento = assinaturaDefinitiva.getDataVencimento();
				for (int i = 1; i < assinaturas.size(); i++) {
					Assinatura assinatura = assinaturas.get(i);
					if (documento == null)
						documento = assinatura.getDocumentoPagamento();
					if (email == null)
						email = assinatura.getEmailPagamento();
					if (telefone == null)
						telefone = assinatura.getTelefone();
					if (assinatura.getDataVencimento().compareTo(dataDeVencimento) > 0) {
						dataDeVencimento = assinatura.getDataVencimento();
					}
					List<AssinaturaPagamento> pagamentos = assinaturaService.getListPagamentos(assinatura);
					for (AssinaturaPagamento pagamento : pagamentos) {
						pagamento.setAssinatura(assinaturaDefinitiva);
						if (documento == null)
							documento = pagamento.getPagamento().getCpf();
						if (email == null)
							email = pagamento.getPagamento().getEmail();
						if (telefone == null)
							telefone = pagamento.getPagamento().getTelefone();
						assinaturaService.gravar(pagamento);
					}
					assinaturaService.excluir(assinatura);
				}
				assinaturaDefinitiva.setDocumentoPagamento(documento);
				assinaturaDefinitiva.setEmailPagamento(email);
				assinaturaDefinitiva.setTelefone(telefone);
				assinaturaDefinitiva.setDataVencimento(dataDeVencimento);
				assinaturaService.gravar(assinaturaDefinitiva);
				System.out.println("Gravando " + assinaturaDefinitiva.getConta().getId());
			}
		}
	}

	private void importarBacktest(int back, String nomeDoArquivo) {
		backtestService.importar(back, nomeDoArquivo);
	}

	private void tratarOperacoes() {
		Router router = Router.getInstance();
		router.setLicencaService(licencaService);
		System.out.println("Aplicacao subiu");

		// List<Assinatura> assinaturas = assinaturaService.getList();
		//
		// for (Assinatura assinatura : assinaturas) {
		// if (assinatura.getConta().getCorretora().contains("Genial")) {
		// assinatura.setPausado(true);
		// assinaturaService.gravar(assinatura);
		// }
		// }

		Map<String, String> mapaContas = new HashMap<>();
		List<Operacao> operacoes = operacaoService.getList();
		for (Operacao operacao : operacoes) {
			mapaContas.put(operacao.getConta().getId(), operacao.getConta().getId());
		}

		List<String> contas = Arrays.asList("9I34F1", "YWLKD2");

		for (String account : mapaContas.keySet()) {

			List<Operacao> compras = operacaoService.getList(account, "C");
			List<Operacao> vendas = operacaoService.getList(account, "V");
			List<Trade> trades = new ArrayList<>();

			for (Operacao compra : compras) {
				if (compra.getVolume().compareTo(BigDecimal.ZERO) > 0) {
					for (Operacao venda : vendas) {
						if (venda.getVolume().compareTo(BigDecimal.ZERO) > 0
								&& compra.getVolume().compareTo(BigDecimal.ZERO) > 0) {
							if (compra.getVolume().compareTo(venda.getVolume()) == 0
									&& compra.getAtivo().equals(venda.getAtivo())) {
								String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
								Double pontos = venda.getPreco() - compra.getPreco();
								Double resultado = pontos * compra.getAtivo().getCategoria().getGanho()
										* compra.getVolume().doubleValue();

								LocalDateTime dataEntrada = null;
								LocalDateTime dataSaida = null;
								Long duracao = null;
								if (compra.getData().compareTo(venda.getData()) < 0) {
									duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
									dataEntrada = compra.getData();
									dataSaida = venda.getData();
								} else {
									duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
									dataEntrada = venda.getData();
									dataSaida = compra.getData();
								}

								Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
										.conta(compra.getConta()).dataEntrada(dataEntrada).dataSaida(dataSaida)
										.direcao(direcao).duracao(duracao)
										.expert((!Objects.isNull(compra.getExpert()) ? compra.getExpert()
												: venda.getExpert()))
										.pontos(pontos).resultado(resultado).venda(venda.getPreco())
										.volume(compra.getVolume()).build();
								trades.add(trade);
								compra.setVolume(BigDecimal.ZERO);
								venda.setVolume(BigDecimal.ZERO);
							} else if (compra.getVolume().compareTo(venda.getVolume()) > 0
									&& compra.getAtivo().equals(venda.getAtivo())) {
								BigDecimal quantidade = venda.getVolume();
								String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
								Double pontos = venda.getPreco() - compra.getPreco();
								Double resultado = pontos * compra.getAtivo().getCategoria().getGanho()
										* quantidade.doubleValue();

								LocalDateTime dataEntrada = null;
								LocalDateTime dataSaida = null;
								Long duracao = null;
								if (compra.getData().compareTo(venda.getData()) < 0) {
									duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
									dataEntrada = compra.getData();
									dataSaida = venda.getData();
								} else {
									duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
									dataEntrada = venda.getData();
									dataSaida = compra.getData();
								}

								Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
										.conta(compra.getConta()).dataEntrada(dataEntrada).dataSaida(dataSaida)
										.direcao(direcao).duracao(duracao)
										.expert((!Objects.isNull(compra.getExpert()) ? compra.getExpert()
												: venda.getExpert()))
										.pontos(pontos).resultado(resultado).venda(venda.getPreco()).volume(quantidade)
										.build();
								trades.add(trade);
								compra.setVolume(compra.getVolume().subtract(quantidade));
								venda.setVolume(venda.getVolume().subtract(quantidade));
							} else if (compra.getVolume().compareTo(venda.getVolume()) < 0
									&& compra.getAtivo().equals(venda.getAtivo())) {
								BigDecimal quantidade = compra.getVolume();
								String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
								Double pontos = venda.getPreco() - compra.getPreco();
								Double resultado = pontos * compra.getAtivo().getCategoria().getGanho()
										* quantidade.doubleValue();

								LocalDateTime dataEntrada = null;
								LocalDateTime dataSaida = null;

								Long duracao = null;
								if (compra.getData().compareTo(venda.getData()) < 0) {
									duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
									dataEntrada = compra.getData();
									dataSaida = venda.getData();
								} else {
									duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
									dataEntrada = venda.getData();
									dataSaida = compra.getData();
								}

								Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
										.conta(compra.getConta()).dataEntrada(dataEntrada).dataSaida(dataSaida)
										.direcao(direcao).duracao(duracao)
										.expert((!Objects.isNull(compra.getExpert()) ? compra.getExpert()
												: venda.getExpert()))
										.pontos(pontos).resultado(resultado).venda(venda.getPreco()).volume(quantidade)
										.build();
								trades.add(trade);
								compra.setVolume(compra.getVolume().subtract(quantidade));
								venda.setVolume(venda.getVolume().subtract(quantidade));
							}
						}
					}
				}
			}

			for (Trade trade : trades) {
				tradeService.gravar(trade);
			}
			for (Operacao operacao : compras) {
				if (operacao.getVolume().compareTo(BigDecimal.ZERO) == 0) {
					operacaoService.excluir(operacao);
				}
			}
			for (Operacao operacao : vendas) {
				if (operacao.getVolume().compareTo(BigDecimal.ZERO) == 0) {
					operacaoService.excluir(operacao);
				}
			}
		}
	}

}
