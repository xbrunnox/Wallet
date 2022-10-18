package br.app.grid.wallet.licenca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicensaService {

	@Autowired
	private LicencaRepository repository;

	public LicencaResponse autenticar(String nome, String corretora, Integer conta) {
		LocalDate data = LocalDate.now();
		Licenca licensa = repository.get(corretora, conta);
		if (licensa == null) {
			licensa = Licenca.builder().conta(conta).corretora(corretora).nome(nome).id(gerarId())
					.dataDeCadastro(LocalDateTime.now()).build();
			repository.save(licensa);
		}
		if (licensa.getDataDeVencimento() == null) {
			return LicencaResponse.builder().ativo(false).id(licensa.getId()).build();
		}
		return LicencaResponse.builder().ativo(data.compareTo(licensa.getDataDeVencimento()) <= 0).id(licensa.getId())
				.build();
	}

	public Boolean isAtivo(String nome, String corretora, Integer conta) {
		LocalDate data = LocalDate.now();
		Licenca licensa = repository.get(corretora, conta);
		if (licensa != null) {
			if (licensa.getDataDeVencimento() == null)
				return null;
			return (data.compareTo(licensa.getDataDeVencimento()) <= 0);
		} else {
			licensa = Licenca.builder().conta(conta).corretora(corretora).nome(nome).id(gerarId()).build();
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
		System.out.println("ID Gerado: "+id);
		return id;
	}

}
