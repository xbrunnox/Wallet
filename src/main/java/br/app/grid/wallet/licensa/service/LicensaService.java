package br.app.grid.wallet.licensa.service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.licensa.Licensa;
import br.app.grid.wallet.licensa.repository.LicensaRepository;

@Service
public class LicensaService {

	@Autowired
	private LicensaRepository repository;

	public Boolean isAtivo(String nome, String corretora, Integer conta) {
		LocalDate data = LocalDate.now();
		Licensa licensa = repository.get(corretora, corretora);
		if (licensa != null) {
			if (licensa.getVencimento() == null)
				return null;
			return (data.compareTo(licensa.getVencimento()) <= 0);
		} else {
			licensa = Licensa.builder().conta(conta).corretora(corretora).nome(nome).id(gerarId()).build();
			repository.save(licensa);
		}
		return false;
	}

	private String gerarId() {
		Random random = new Random();
		String id = "";
		String letras = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < 4; i++) {
			String letra = letras.charAt(random.nextInt(letras.length())) + "";
			id += letra;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			id += random.nextInt(10);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

}
