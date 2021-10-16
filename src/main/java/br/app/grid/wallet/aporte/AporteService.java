package br.app.grid.wallet.aporte;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.carteira.Carteira;

@Service
public class AporteService {
	
	@Autowired
	private AporteRepository aporteRepository;
	
	public List<Aporte> getList(Carteira carteira) {
		if (carteira == null)
			return new ArrayList<>();
		return getList(carteira.getId());
	}

	private List<Aporte> getList(int idCarteira) {
		return aporteRepository.getListByIdCarteira(idCarteira);
	}
	
	

}
