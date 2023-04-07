package br.app.grid.wallet.servidor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.enums.ServidorTipoEnum;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de fevereiro de 2023.
 */
@Service
public class ServidorService {
	
	@Autowired
	private ServidorRepository repository;
	
	
	public Servidor gravar(Servidor servidor) {
		return repository.save(servidor);
	}


	public List<Servidor> getList(ServidorTipoEnum tipo, Boolean ativo) {
		return repository.getList(tipo, ativo);
	}

}
