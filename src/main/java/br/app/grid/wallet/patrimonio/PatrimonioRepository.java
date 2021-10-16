package br.app.grid.wallet.patrimonio;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrimonioRepository extends CrudRepository<Patrimonio, Integer>{

	@Query("FROM Patrimonio p WHERE p.carteira.id = :idCarteira")
	List<Patrimonio> getList(int idCarteira);

}
