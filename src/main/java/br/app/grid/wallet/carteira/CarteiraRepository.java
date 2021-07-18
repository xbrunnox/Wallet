package br.app.grid.wallet.carteira;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraRepository extends CrudRepository<Carteira, Integer> {

	@Query("FROM Carteira c WHERE c.id = :id")
	public Carteira getById(@Param("id") Integer id);

}
