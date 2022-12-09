package br.app.grid.wallet.assinatura.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;

@Repository
public interface AssinaturaAtivaRepository extends CrudRepository<AssinaturaAtivaView, Integer>{

	@Query("FROM AssinaturaAtivaView aav WHERE aav.dataVencimento >= :data ORDER BY aav.nome")
	public List<AssinaturaAtivaView> getList(LocalDate data);
}
