package br.app.grid.wallet.dividendo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.ativo.Ativo;

public interface DividendoRepository extends CrudRepository<Dividendo, Integer>{

	@Query("FROM Dividendo div WHERE div.ativo = :ativo ORDER BY div.dataBase DESC")
	public List<Dividendo> getUltimoDividendo(Ativo ativo);

}
