package br.app.grid.wallet.historico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.aporte.Aporte;
import br.app.grid.wallet.aporte.AporteService;
import br.app.grid.wallet.carteira.Carteira;
import br.app.grid.wallet.carteira.CarteiraService;
import br.app.grid.wallet.patrimonio.Patrimonio;
import br.app.grid.wallet.patrimonio.PatrimonioService;

@Service
public class HistoricoService {

	@Autowired
	private AporteService aporteService;

	@Autowired
	private CarteiraService carteiraService;
	
	@Autowired
	private PatrimonioService patrimonioService;

	public Historico get(int idCarteira) {
		Historico historico = new Historico();
		Carteira carteira = carteiraService.get(idCarteira);
		List<Aporte> aportes = aporteService.getList(carteira);
		List<Patrimonio> patrimonios = patrimonioService.getList(carteira);
		historico.add(aportes);
		historico.addPatrimonios(patrimonios);
		
		historico.setAporteMensal(carteira.getAporteMensal());
		historico.setRendimentoEsperado(carteira.getRendimentoEsperado());
		historico.atualizar();

		return historico;
	}

}
