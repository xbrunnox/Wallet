package br.app.grid.wallet.assinatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;

@Repository
public interface AssinaturaPendenciaRepository extends CrudRepository<AssinaturaPendenciaView, Integer>{

	@Query("FROM AssinaturaPendenciaView apv ORDER BY apv.nome")
	public List<AssinaturaPendenciaView> getList();
}
