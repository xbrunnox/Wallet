package br.app.grid.wallet.aporte;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AporteRepository extends CrudRepository<Aporte, Integer> {

	@Query("FROM Aporte a WHERE a.carteira.id = :idCarteira ORDER BY a.data")
	List<Aporte> getListByIdCarteira(int idCarteira);
	
	

}
