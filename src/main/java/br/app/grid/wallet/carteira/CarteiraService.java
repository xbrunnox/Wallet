package br.app.grid.wallet.carteira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarteiraService {
	
	@Autowired
	private CarteiraRepository repository;
	
	public Carteira get(int id) {
		return repository.getById(id);
	}

}
