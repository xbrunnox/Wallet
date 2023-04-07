package br.app.grid.wallet.licenca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.repository.AssinaturaRepository;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.util.CorretoraUtil;

@Service
public class ContaService {

	@Autowired
	private ContaRepository repository;

	@Autowired
	private AssinaturaRepository assinaturaRepository;
	
	@Autowired
	private AssinaturaService assinaturaService;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public ContaResponse autenticar(String nome, String corretora, Integer conta) {
		Conta licenca = repository.get(corretora, conta);
		if (licenca == null) {
			licenca = repository.get(CorretoraUtil.format(corretora), conta);
		}
		if (licenca == null) {
			licenca = Conta.builder().conta(conta).corretora(CorretoraUtil.format(corretora)).nome(nome).id(gerarId())
					.dataDeCadastro(LocalDateTime.now()).build();
			repository.save(licenca);
		}
		return autenticar(licenca.getId());
	}

	public ContaResponse autenticar(String licenceKey) {
		List<Assinatura> assinaturas = assinaturaRepository.getListAtivas(licenceKey, LocalDate.now());
		if (assinaturas.size() == 0)
			return ContaResponse.builder().ativo(false).id(licenceKey).build();

		LocalDate maiorVencimento = assinaturas.get(0).getDataVencimento();
		Assinatura assinaturaMaiorVencimento = assinaturas.get(0);
		for (Assinatura assinatura : assinaturas) {
			if (assinatura.getDataVencimento().compareTo(maiorVencimento) > 0) {
				maiorVencimento = assinatura.getDataVencimento();
				assinaturaMaiorVencimento = assinatura;
			}
		}

		Servidor servidor = assinaturaMaiorVencimento.getServidor();
		if (servidor == null) {
			servidor = assinaturaService.getServidorParaAlocacao();
			assinaturaMaiorVencimento.setServidor(servidor);
			assinaturaService.gravar(assinaturaMaiorVencimento);
		}

		return ContaResponse.builder().ativo(true).id(licenceKey).expiracao(formatter.format(maiorVencimento))
				.servidor(servidor.getHostname()).porta(servidor.getPorta()).build();
	}

	public Boolean isAtivo(String nome, String corretora, Integer conta) {
		LocalDate data = LocalDate.now();
		Conta licensa = repository.get(corretora, conta);
		if (licensa != null) {
			if (licensa.getDataDeVencimento() == null)
				return null;
			return (data.compareTo(licensa.getDataDeVencimento()) <= 0);
		} else {
			licensa = Conta.builder().conta(conta).corretora(corretora).nome(nome).id(gerarId()).build();
			repository.save(licensa);
		}
		return false;
	}

	private String gerarId() {
		Random random = new Random();
		String id = "";
		String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		for (int i = 0; i < 6; i++) {
			String letra = letras.charAt(random.nextInt(letras.length())) + "";
			id += letra;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ID Gerado: " + id);
		return id;
	}

	public Conta get(String licenseKey) {
		return repository.findById(licenseKey).get();
	}

	public ContaInfoResponse info(String license) {
		Conta conta = get(license);
		if (conta == null) {
			return ContaInfoResponse.builder().ativo(false).conta(license).pausado(true).build();
		}
		List<Assinatura> assinaturas = assinaturaRepository.getListAtivas(license, LocalDate.now());
		if (assinaturas.size() == 0)
			return ContaInfoResponse.builder().ativo(false).conta(conta.getId()).nome(conta.getNome()).pausado(true)
					.corretora(conta.getCorretora()).build();

		LocalDate maiorVencimento = assinaturas.get(0).getDataVencimento();
		Assinatura ass = assinaturas.get(0);
		for (Assinatura assinatura : assinaturas) {
			if (assinatura.getDataVencimento().compareTo(maiorVencimento) > 0) {
				maiorVencimento = assinatura.getDataVencimento();
				ass = assinatura;
			}
		}

		return ContaInfoResponse.builder().ativo(true).conta(conta.getId()).nome(conta.getNome())
				.corretora(conta.getCorretora()).pausado(ass.isPausado()).expiracao(maiorVencimento.format(formatter))
				.maquina((Objects.isNull(ass.getMaquina()) ? null : ass.getMaquina().getNome())).build();
	}

	public List<ContaResultadoView> getListContaResultado() {
		return repository.getListContaResultado();
	}

	public List<Conta> getList() {
		return repository.getList();
	}

	public void excluir(String idConta) {
		Conta conta = get(idConta);
		repository.delete(conta);
	}

}
