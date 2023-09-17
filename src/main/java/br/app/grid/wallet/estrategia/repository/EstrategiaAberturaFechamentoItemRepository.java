package br.app.grid.wallet.estrategia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.app.grid.wallet.estrategia.EstrategiaAberturaFechamentoItem;

/**
 * <b>EstrategiaAberturaFechamentoItemRepository</b><br>
 * Repository para a entidade EstrategiaAberturaFechamentoItem.
 * 
 * @since 05 de setembro de 2023.
 * @author Brunno José Guimarães de Almeida.
 */
public interface EstrategiaAberturaFechamentoItemRepository
		extends CrudRepository<EstrategiaAberturaFechamentoItem, Integer> {

	/**
	 * Retorna a lista de EstrategiaAberturaFechamentoItem ordenados pelo código do ativo.
	 * @return Lista de EstrategiaAberturaFechamentoItem.
	 */
	@Query("FROM EstrategiaAberturaFechamentoItem est ORDER BY est.ativo.codigo")
	List<EstrategiaAberturaFechamentoItem> getList();

}
