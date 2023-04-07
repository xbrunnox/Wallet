package br.app.grid.wallet.servidor;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.enums.ServidorTipoEnum;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de fevereiro de 2023.
 */
public interface ServidorRepository extends CrudRepository<Servidor, Integer> {

	@Query(value = "FROM Servidor serv WHERE serv.tipo = :tipo AND serv.ativo = :ativo")
	List<Servidor> getList(ServidorTipoEnum tipo, Boolean ativo);

	@Query(value = "FROM ServidorAlocacao sa ORDER BY sa.total")
	List<ServidorAlocacao> getListAlocacao();

	@Query(value = "FROM Servidor serv WHERE serv.id = :id")
	Servidor getById(Integer id);

}
