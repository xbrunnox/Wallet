package br.app.grid.wallet.assinatura.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.AssinaturaExpertsResponse;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.repository.AssinaturaAtivaRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaExpertRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaPagamentoRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaPendenciaRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaRepository;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;
import br.app.grid.wallet.assinatura.view.AssinaturaView;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaRepository;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoRepository;
import br.app.grid.wallet.produto.Produto;
import br.app.grid.wallet.produto.ProdutoService;
import br.app.grid.wallet.produto.ProdutoTipo;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboRepository;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.servidor.ServidorAlocacao;
import br.app.grid.wallet.servidor.ServidorRepository;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.repository.TradeRepository;

@Service
public class AssinaturaService {

	@Autowired
	private AssinaturaRepository assinaturaRepository;

	@Autowired
	private AssinaturaPagamentoRepository assinaturaPagamentoRepository;

	@Autowired
	private AssinaturaAtivaRepository assinaturaAtivaRepository;

	@Autowired
	private AssinaturaPendenciaRepository assinaturaPendenciaRepository;

	@Autowired
	private AssinaturaExpertRepository assinaturaExpertRepository;

	@Autowired
	private RoboRepository roboRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TradeRepository tradeRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	public AssinaturaExpertsResponse getExpertsAtivos(String conta) {
		return AssinaturaExpertsResponse.builder()
				.experts(assinaturaExpertRepository.getListAtivados(conta, LocalDate.now())).conta(conta).build();
	}

	public List<Assinatura> getList() {
		return assinaturaRepository.getList();
	}

	public List<Assinatura> getListAtivas() {
		LocalDate data = LocalDate.now();
		return assinaturaRepository.getListAtivas(data);
	}

	public List<Conta> getContasInativas() {
//		List<AssinaturaAtivaView> assinaturasAtivas = assinaturaAtivaRepository.getList(LocalDate.now());
		List<AssinaturaView> assinaturas = assinaturaRepository.getListView();
//		List<Assinatura> assinaturasAtivas = getListAtivas();
		Map<String, AssinaturaView> mapaAssinaturas = new HashMap<>();
		for (AssinaturaView assinatura : assinaturas) {
			if (assinatura.getDataVencimento().compareTo(LocalDate.now()) >= 0)
				mapaAssinaturas.put(assinatura.getConta(), assinatura);
		}
		List<Conta> contas = contaRepository.getList();
		for (int i = contas.size() - 1; i >= 0; i--) {
			Conta conta = contas.get(i);
			if (mapaAssinaturas.containsKey(conta.getId())) {
				contas.remove(i);
			}
		}
		return contas;
	}

	public Assinatura ativar(String idConta, Long idPagamento) {
		Conta conta = contaRepository.findById(idConta).get();
		Pagamento pagamento = pagamentoRepository.findById(idPagamento).get();

		Robo dolar = roboRepository.findById("WDO01").get();
		Robo indice = roboRepository.findById("WIN01").get();
		Robo delta = roboRepository.findById("DELTA").get();

		if (conta != null && pagamento != null) {
			Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
					.dataVencimento(LocalDate.now().plusMonths(1))
					.documentoPagamento(DocumentoFormatter.format(pagamento.getCpf()))
					.emailPagamento(pagamento.getEmail().toLowerCase()).telefone(pagamento.getTelefone())
					.servidor(getServidorParaAlocacao()).pausado(true).build();
			assinaturaRepository.save(assinatura);
			// Expert Dolar
			AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(dolar)
					.volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
//			assinaturaExpertRepository.save(expertDolar);
			// Expert Indice
			AssinaturaExpert expertIndice = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
					.expert(indice).volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
//			assinaturaExpertRepository.save(expertIndice);
			// Expert Delta
			AssinaturaExpert expertDelta = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(delta)
					.volume(delta.getVolume()).volumeMaximo(delta.getVolume()).build();
			assinaturaExpertRepository.save(expertDelta);
			assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
					.dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());

			pagamento.setAssociado(true);
			pagamentoRepository.save(pagamento);
			return assinatura;
		}

		return null;
	}

	/**
	 * Retorna o próximo servidor para alocação, ou seja, o servidor com menos
	 * usuários
	 * 
	 * @return Servidor.
	 */
	public Servidor getServidorParaAlocacao() {
		List<ServidorAlocacao> alocacoes = servidorRepository.getListAlocacao();
		Servidor servidor = servidorRepository.getById(alocacoes.get(0).getId());
		return servidor;
	}

	public Assinatura ativarComPendencia(String idConta) {
		Conta conta = contaRepository.findById(idConta).get();

		Robo dolar = roboRepository.findById("WDO01").get();
		Robo indice = roboRepository.findById("WIN01").get();
		Robo delta = roboRepository.findById("DELTA").get();

		if (conta != null) {
			Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
					.dataVencimento(LocalDate.now().plusMonths(1)).servidor(getServidorParaAlocacao()).pausado(true)
					.build();
			assinaturaRepository.save(assinatura);
			// Expert Dolar
			AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(dolar)
					.volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
//						assinaturaExpertRepository.save(expertDolar);
			// Expert Indice
			AssinaturaExpert expertIndice = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
					.expert(indice).volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
//						assinaturaExpertRepository.save(expertIndice);
			// Expert Delta
			AssinaturaExpert expertDelta = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(delta)
					.volume(delta.getVolume()).volumeMaximo(delta.getVolume()).build();
			assinaturaExpertRepository.save(expertDelta);

			return assinatura;
		}

		return null;
	}

	public List<AssinaturaPendenciaView> getPendencias() {
		return assinaturaPendenciaRepository.getList();
	}

	public List<AssinaturaAtivaView> getAtivas() {
		return assinaturaAtivaRepository.getList(LocalDate.now());
	}

	public void migrar(String origem, String destino) {
		List<Assinatura> assinaturas = assinaturaRepository.getList(origem);
		List<Assinatura> assinaturasDestino = assinaturaRepository.getList(destino);
		Assinatura assinaturaDestino = (assinaturasDestino.size() == 1 ? assinaturasDestino.get(0) : null);
		Conta contaDestino = contaRepository.findById(destino).get();
		if (assinaturaDestino == null) {
			for (Assinatura assinatura : assinaturas) {
				assinatura.setConta(contaDestino);
				assinatura.setPausado(false);
				assinaturaRepository.save(assinatura);
			}
			List<Trade> trades = tradeRepository.getList(origem);
			for (Trade trade : trades) {
				trade.setConta(contaDestino);
				tradeRepository.save(trade);
			}
		} else {
			List<Trade> trades = tradeRepository.getList(origem);
			for (Trade trade : trades) {
				trade.setConta(contaDestino);
				tradeRepository.save(trade);
			}
			List<AssinaturaPagamento> pagamentos = assinaturaPagamentoRepository.getList(assinaturas.get(0).getId());
			for (AssinaturaPagamento pagamento : pagamentos) {
				pagamento.setAssinatura(assinaturaDestino);
				assinaturaPagamentoRepository.save(pagamento);
			}
			assinaturaRepository.delete(assinaturas.get(0));
		}
	}

	public void gravar(Assinatura assinatura) {
		assinaturaRepository.save(assinatura);
	}

	public Assinatura get(Integer idAssinatura) {
		return assinaturaRepository.get(idAssinatura);
	}

	public Assinatura associarPagamento(Integer idAssinatura, Long idPagamento) {
		Assinatura assinatura = assinaturaRepository.get(idAssinatura);
		Pagamento pagamento = pagamentoRepository.findById(idPagamento).get();

		assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
				.dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());

		pagamento.setAssociado(true);
		pagamentoRepository.save(pagamento);

		return assinatura;
	}

	public void identificarPagamento(Long idPagamento) {
		if (idPagamento == null || idPagamento == 0)
			return;
		Pagamento pagamento = pagamentoRepository.get(idPagamento);
		if (pagamento != null) {
			if (pagamento.isAssociado())
				return;
			Produto produto = produtoService.getByNome(pagamento.getProduto());
			if (produto != null) {
				if (produto.getTipo() == ProdutoTipo.ASSINATURA_MENSAL) {
					List<Assinatura> assinaturas = assinaturaRepository
							.getListByEmail(pagamento.getEmail().toLowerCase());
					Assinatura assinatura = null;
					for (Assinatura ass : assinaturas) {
						if (assinatura == null
								|| ass.getDataVencimento().compareTo(assinatura.getDataVencimento()) > 0) {
							assinatura = ass;
						}
					}
					if (assinatura != null) {
						AssinaturaPagamento assPagamento = AssinaturaPagamento.builder().assinatura(assinatura)
								.dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build();
						assinaturaPagamentoRepository.save(assPagamento);
						pagamento.setAssociado(true);
						pagamentoRepository.save(pagamento);
						if (assinatura.getDataVencimento()
								.compareTo(pagamento.getDataAtualizacao().toLocalDate()) > 0) {
							assinatura.setDataVencimento(assinatura.getDataVencimento().plusMonths(1));
						} else {
							assinatura.setDataVencimento(pagamento.getDataAtualizacao().toLocalDate().plusMonths(1));
						}
						assinaturaRepository.save(assinatura);
						System.out.println("Pagamento identificado.[" + idPagamento + "] Assinatura encontrada. ["
								+ assinatura.getConta().getId() + "]");
						
						// Atualizando subcontas
						List<Assinatura> assinaturaSubContas = getListSubContas(assinatura.getId());
						if (!Objects.isNull(assinaturaSubContas)) {
							for (Assinatura subAssinatura : assinaturaSubContas) {
								subAssinatura.setDataVencimento(assinatura.getDataVencimento());
								assinaturaRepository.save(subAssinatura);
							}
						}
					} else {
						System.out.println("Pagamento não identificado. Assinatura não encontrada.");
					}
				}
			}
		}

	}

	public List<Assinatura> getList(String idConta) {
		return assinaturaRepository.getList(idConta);
	}

	public List<AssinaturaPagamento> getListPagamentos(String conta) {
		return assinaturaPagamentoRepository.getListPagamentos(conta);
	}

	public List<AssinaturaPagamento> getListPagamentos(Assinatura assinatura) {
		return assinaturaPagamentoRepository.getListPagamentos(assinatura.getId());
	}

	public void gravar(AssinaturaPagamento pagamento) {
		assinaturaPagamentoRepository.save(pagamento);

	}

	public void excluir(Assinatura assinatura) {
		assinaturaRepository.delete(assinatura);
	}

	public List<AssinaturaExpert> getExperts(Assinatura assinatura) {
		return assinaturaExpertRepository.getList(assinatura);
	}

	public void excluir(AssinaturaExpert expert) {
		assinaturaExpertRepository.delete(expert);

	}

	public void gravar(AssinaturaExpert expert) {
		assinaturaExpertRepository.save(expert);
	}

	/**
	 * Retorna as assinaturas com vencimento a partir da data indicada (inclusive).
	 * 
	 * @param data Data.
	 * @return Lista de assinaturas.
	 */
	public List<Assinatura> getList(LocalDate data) {
		return assinaturaRepository.getList(data);
	}

	public List<AssinaturaView> getListView() {
		return assinaturaRepository.getListView();
	}

	public Assinatura getByConta(String conta) {
		List<Assinatura> assinaturas = getList(conta);
		if (assinaturas.size() > 0)
			return assinaturas.get(0);
		return null;
	}

	public List<Assinatura> getListSubContas(Integer idAssinatura) {
		return assinaturaRepository.getListSubContas(idAssinatura);
	}
}