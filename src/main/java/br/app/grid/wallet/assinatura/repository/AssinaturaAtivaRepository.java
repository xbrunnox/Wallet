package br.app.grid.wallet.assinatura.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;

public interface AssinaturaAtivaRepository extends CrudRepository<AssinaturaAtivaView, Integer> {

  @Query("FROM AssinaturaAtivaView aav WHERE aav.dataVencimento >= :data ORDER BY aav.nome")
  public List<AssinaturaAtivaView> getList(LocalDate data);

  /**
   * Retorna a lista de assinaturas ativas.
   * 
   * @param idAfiliado ID do Afiliado.
   * @param dataVencimento Data de vencimento.
   * @return Lista de assinatura ativas.
   */
  @Query("FROM AssinaturaAtivaView aav WHERE aav.idAfiliado = :idAfiliado AND aav.dataVencimento >= :dataAtual ORDER BY aav.nome")
  public List<AssinaturaAtivaView> getList(Integer idAfiliado, LocalDate dataAtual);
}
