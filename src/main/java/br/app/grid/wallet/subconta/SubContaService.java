package br.app.grid.wallet.subconta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.repository.AssinaturaExpertRepository;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;

/**
 * <b>SubContaService</b><br>
 * Service responsável pelas operações em subcontas.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 18 de abril de 2023.
 */
@Service
public class SubContaService {

	@Autowired
	private RoboService automacaoService;

	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private AssinaturaExpertRepository assinaturaExpertService;

	@Autowired
	private ContaService contaService;

	/**
	 * Realiza a associação da conta indicada à assinatura indicada.
	 * 
	 * @param idAssinatura ID da assinatura principal.
	 * @param idConta      ID da conta.
	 * @param idAutomacao  ID da automação.
	 */
	public void associar(Integer idAssinatura, String idConta, String idAutomacao) {
		Assinatura assinaturaPrincipal = assinaturaService.get(idAssinatura);
		if (assinaturaPrincipal.getAssinaturaPrincipal() != null) {
			throw new RuntimeException("Essa conta não é a principal.");
		}

		Robo automacao = automacaoService.getById(idAutomacao);

		Conta conta = contaService.get(idConta);

		Assinatura assinaturaSubconta = assinaturaService.getByConta(idConta);
		if (Objects.isNull(assinaturaSubconta)) {
			// Conta não possui assinatura
			assinaturaSubconta = Assinatura.builder().assinaturaPrincipal(assinaturaPrincipal).conta(conta)
					.dataCadastro(LocalDateTime.now()).dataVencimento(assinaturaPrincipal.getDataVencimento())
					.pausado(false).servidor(assinaturaService.getServidorParaAlocacao()).build();
			assinaturaService.gravar(assinaturaSubconta);
			// Adição de expert.
			AssinaturaExpert expert = AssinaturaExpert.builder().assinatura(assinaturaSubconta).ativado(true)
					.expert(automacao).volume(automacao.getVolume()).volumeMaximo(automacao.getVolume()).build();
			assinaturaService.gravar(expert);
		} else {
			// Conta já possui assinatura
			List<AssinaturaExpert> experts = assinaturaExpertService.getList(assinaturaSubconta);
			// Remoção de experts anteriores;
			for (AssinaturaExpert expert : experts) {
				assinaturaExpertService.delete(expert);
			}
			// Adição de expert.
			AssinaturaExpert expert = AssinaturaExpert.builder().assinatura(assinaturaSubconta).ativado(true)
					.expert(automacao).volume(automacao.getVolume()).volumeMaximo(automacao.getVolume()).build();
			assinaturaService.gravar(expert);
			// Associação da Assinatura Principal
			assinaturaSubconta.setAssinaturaPrincipal(assinaturaPrincipal);
			assinaturaSubconta.setDataVencimento(assinaturaPrincipal.getDataVencimento());
			assinaturaSubconta.setServidor(assinaturaService.getServidorParaAlocacao());
			assinaturaService.gravar(assinaturaSubconta);
		}
	}

	/**
	 * Retorna a lista de subcontas da assinatura indicada.
	 * 
	 * @param idAssinatura ID da Assinatura.
	 * @return Lista de assinaturas que são subcontas.
	 */
	public List<Assinatura> getList(Integer idAssinatura) {
		return assinaturaService.getListSubContas(idAssinatura);
	}

}
