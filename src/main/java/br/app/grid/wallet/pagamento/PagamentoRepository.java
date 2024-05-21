package br.app.grid.wallet.pagamento;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.afiliado.Afiliado;

/**
 * <b>PagamentoRepository</b><br>
 * Repository responsável pelas operações com a entidade Pagamento.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

  @Query("FROM Pagamento pag WHERE pag.associado = false ORDER BY pag.nome")
  List<Pagamento> getListNaoAssociados();

  @Query("FROM Pagamento pag ORDER BY pag.dataAtualizacao")
  List<Pagamento> getList();

  @Query("FROM Pagamento pag WHERE pag.id = :idPagamento")
  Pagamento get(Long idPagamento);

  @Query("FROM Pagamento pag WHERE pag.afiliado = :afiliado ORDER BY pag.dataAtualizacao")
  List<Pagamento> getList(Afiliado afiliado);

  @Query("FROM Pagamento pag WHERE pag.afiliado = :afiliado AND pag.associado = false ORDER BY pag.nome")
  List<Pagamento> getListNaoAssociado(Afiliado afiliado);

  @Query("FROM Pagamento pag WHERE pag.afiliado = :afiliado AND pag.id = :idPagamento")
  Pagamento get(Long idPagamento, Afiliado afiliado);

}
