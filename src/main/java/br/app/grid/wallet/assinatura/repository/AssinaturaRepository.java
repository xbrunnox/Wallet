package br.app.grid.wallet.assinatura.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.view.AssinaturaResultadoView;
import br.app.grid.wallet.assinatura.view.AssinaturaView;

/**
 * <b>AssinaturaRepository</b><br>
 * Repository responsável pelas operações de banco de dados da entidade Assinatura.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
public interface AssinaturaRepository extends CrudRepository<Assinatura, Integer> {

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

  @Query("FROM Assinatura ass WHERE ass.emailPagamento = :email")
  public List<Assinatura> getListByEmail(String email);

  @Query("FROM Assinatura ass WHERE ass.emailPagamento = :email AND ass.desabilitada = false")
  public List<Assinatura> getListHabilitadasByEmail(String email);

  @Query("FROM Assinatura ass WHERE ass.dataVencimento >= :data")
  public List<Assinatura> getList(LocalDate data);

  @Query("From AssinaturaView ass ORDER BY ass.nome")
  public List<AssinaturaView> getListView();

  @Query("From AssinaturaView ass WHERE ass.idAfiliado = :idAfiliado AND ass.dataVencimento >= :dataDeVencimento ORDER BY ass.nome")
  public List<AssinaturaView> getListView(Integer idAfiliado, LocalDate dataDeVencimento);

  @Query("FROM Assinatura ass WHERE ass.assinaturaPrincipal.id = :idAssinatura")
  public List<Assinatura> getListSubContas(Integer idAssinatura);

  @Query("From AssinaturaResultadoView ass WHERE ass.idAfiliado = :idAfiliado")
  public List<AssinaturaResultadoView> getListResultadosView(Integer idAfiliado);

  @Query("From AssinaturaView ass WHERE ass.idAfiliado = :idAfiliado AND ass.dataVencimento < :dataCorrente ORDER BY ass.nome")
  public List<AssinaturaView> getListInativasView(Integer idAfiliado, LocalDate dataCorrente);

  /**
   * Retorna a lista de assinaturas do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @return Lista de assinaturas do afiliado.
   */
  @Query("FROM Assinatura ass WHERE ass.afiliado = :afiliado ORDER BY ass.conta.nome")
  public List<Assinatura> getList(Afiliado afiliado);

  @Query("FROM Assinatura ass WHERE ass.afiliado = :afiliado AND ass.conta.id = :license")
  public Assinatura getByContaAfiliado(String license, Afiliado afiliado);

}
