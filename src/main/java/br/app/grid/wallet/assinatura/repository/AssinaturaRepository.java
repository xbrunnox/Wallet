package br.app.grid.wallet.assinatura.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.assinatura.Assinatura;

@Repository
public interface AssinaturaRepository extends CrudRepository<Assinatura, Integer>{

	@Query("FROM Assinatura ass ORDER BY ass.conta.nome")
	public List<Assinatura> getList();

	@Query("FROM Assinatura ass WHERE ass.dataVencimento >= :data")
	public List<Assinatura> getListAtivas(LocalDate data);

	@Query("FROM Assinatura ass WHERE ass.conta.id = :conta AND ass.dataVencimento >= :data")
	public List<Assinatura> getListAtivas(String conta, LocalDate data);

	@Query("FROM Assinatura ass WHERE ass.conta.id = :conta")
	public List<Assinatura> getList(String conta);

	@Query("FROM Assinatura ass WHERE ass.id = :idAssinatura")
	public Assinatura get(Integer idAssinatura);

	
}
