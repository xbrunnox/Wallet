package br.app.grid.wallet.posicao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PosicaoRepository extends CrudRepository<Posicao, Integer> {

	@Query("FROM Posicao p WHERE p.carteira.id = :idCarteira")
	public List<Posicao> getList(@Param("idCarteira") Integer idCarteira);

}
