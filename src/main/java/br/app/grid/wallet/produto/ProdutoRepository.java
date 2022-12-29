package br.app.grid.wallet.produto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends CrudRepository<Produto, Integer>  {

	@Query("FROM Produto p WHERE p.nome = :produto")
	Produto getByNome(String produto);

}
