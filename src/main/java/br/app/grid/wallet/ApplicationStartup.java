package br.app.grid.wallet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.backtest.service.BacktestService;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.service.OperacaoService;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.socket.Router;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ContaService licencaService;

	@Autowired
	private AssinaturaService assinaturaService;
	
	@Autowired
	private BacktestService backtestService;

	@Autowired
	private OperacaoService operacaoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private TradeService tradeService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println(LocalDate.now());
//		importarBacktest(2, "indice520560.txt");

//		 tratarOperacoes();

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
	}

	private void importarBacktest(int back, String nomeDoArquivo) {
		backtestService.importarArquivo(back, nomeDoArquivo);
	}

	private void tratarOperacoes() {
		Router router = Router.getInstance();
		router.setLicencaService(licencaService);
		System.out.println("Aplicacao subiu");

//		List<Assinatura> assinaturas = assinaturaService.getList();
//		
//		for (Assinatura assinatura : assinaturas) {
//			if (assinatura.getConta().getCorretora().contains("Genial")) {
//				assinatura.setPausado(true);
//				assinaturaService.gravar(assinatura);
//			}
//		}

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
										.direcao(direcao).duracao(duracao).expert(compra.getExpert()).pontos(pontos)
										.resultado(resultado).venda(venda.getPreco()).volume(compra.getVolume())
										.build();
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
										.direcao(direcao).duracao(duracao).expert(compra.getExpert()).pontos(pontos)
										.resultado(resultado).venda(venda.getPreco()).volume(quantidade).build();
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
										.direcao(direcao).duracao(duracao).expert(compra.getExpert()).pontos(pontos)
										.resultado(resultado).venda(venda.getPreco()).volume(quantidade).build();
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
