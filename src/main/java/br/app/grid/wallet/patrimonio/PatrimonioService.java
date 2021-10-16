package br.app.grid.wallet.patrimonio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.carteira.Carteira;

@Service
public class PatrimonioService {
	
	@Autowired
	private PatrimonioRepository patrimonioRepository;
	
	public List<Patrimonio> getList(Carteira carteira) {
		if (carteira == null)
			return new ArrayList<>();
		return patrimonioRepository.getList(carteira.getId());
	}

}
