package br.app.grid.wallet.operacao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.operacao.Operacao;

/**
 * @author Brunno José Guimarães de Almeida
 * @since 20 de dezembro de 2022.
 */
@Repository
public interface OperacaoRepository extends CrudRepository<Operacao, Integer> {

	@Query("FROM Operacao d ORDER BY d.data DESC")
	List<Operacao> getList(Pageable page);

	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND op.expert.id = :expert "
			+ "AND op.ativo.codigo = :ativo AND op.tipo = :tipo AND DATE(op.data) = :date")
	Operacao get(String conta, String expert, String ativo, String tipo, Date date);

	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND op.expert.id = :expert "
			+ "AND op.ativo.codigo = :ativo AND op.tipo = :tipo AND DATE(op.data) = :date")
	List<Operacao> getList(String conta, String expert, String ativo, String tipo, Date date);

	@Query("FROM Operacao d ORDER BY d.data DESC")
	List<Operacao> getList();

	@Query("FROM Operacao d WHERE d.id = :id")
	Operacao get(int id);

	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta")
	List<Operacao> getList(String conta);

	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND op.direcao = :direcao")
	List<Operacao> getList(String conta, String direcao);

	@Query("SELECT op FROM Operacao op WHERE op.conta.id = :conta AND DATE(op.data) = :date")
	List<Operacao> getList(String conta, Date date);

}
