package br.app.grid.wallet.assinatura.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaRepository;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoRepository;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboRepository;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.TradeRepository;

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
	private ContaRepository contaRepository;

	@Autowired
	private TradeRepository tradeRepository;

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
		List<Assinatura> assinaturasAtivas = getListAtivas();
		Map<String, Assinatura> mapaAssinaturas = new HashMap<>();
		for (Assinatura assinatura : assinaturasAtivas) {
			mapaAssinaturas.put(assinatura.getConta().getId(), assinatura);
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

		if (conta != null && pagamento != null) {
			Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
					.dataVencimento(LocalDate.now().plusMonths(1)).build();
			assinaturaRepository.save(assinatura);
			AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(dolar)
					.volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
			AssinaturaExpert expertIndice = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
					.expert(indice).volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
			assinaturaExpertRepository.save(expertDolar);
			assinaturaExpertRepository.save(expertIndice);
			assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
					.dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());

			pagamento.setAssociado(true);
			pagamentoRepository.save(pagamento);
			return assinatura;
		}

		return null;
	}

	public Assinatura ativarComPendencia(String idConta) {
		Conta conta = contaRepository.findById(idConta).get();

		Robo dolar = roboRepository.findById("WDO01").get();
		Robo indice = roboRepository.findById("WIN01").get();

		if (conta != null) {
			Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
					.dataVencimento(LocalDate.now().plusMonths(1)).build();
			assinaturaRepository.save(assinatura);
			AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(dolar)
					.volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
			AssinaturaExpert expertIndice = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
					.expert(indice).volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
			assinaturaExpertRepository.save(expertDolar);
			assinaturaExpertRepository.save(expertIndice);

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

}
