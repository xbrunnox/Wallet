package br.app.grid.wallet.homebroker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>HomeBrokerService</b><br>
 * Service responsável pelas operações com a entidade HomeBroker.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 16 de abril de 2023.
 */
@Service
public class HomeBrokerService {
	
	@Autowired
	private HomeBrokerRepository homeBrokerRepository;
	
	/**
	 * Retorna a lista de HomeBroker.
	 * @return Lista de HomeBroker.
	 */
	public List<HomeBroker> getList(){
		return homeBrokerRepository.getList();
	}

}
