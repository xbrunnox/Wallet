package br.app.grid.wallet.licenca;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * <b>ContaRepository</b><br>
 * Repository responsável pelas operações de banco de dados relativas à entidade Conta.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
public interface ContaRepository extends CrudRepository<Conta, String> {

  @Query("FROM Conta l WHERE l.corretora = :corretora AND l.conta = :conta")
  public Conta get(String corretora, Integer conta);

  @Query("FROM ContaResultadoView cr ORDER BY cr.nome, cr.corretora")
  public List<ContaResultadoView> getListContaResultado();

  @Query("FROM Conta conta ORDER BY conta.id")
  public List<Conta> getList();

  /**
   * Retorna a conta com o ID indicado.
   * 
   * @param id ID da conta.
   * @return Conta com o ID indicado.
   */
  @Query("FROM Conta l WHERE l.id = :id")
  public Conta get(String id);

  /**
   * Retorna a lista de resultados por conta do afiliado indicado.
   * 
   * @param idAfiliado ID do afiliado.
   * @return Lista de resultados.
   */
  @Query("FROM ContaResultadoView cr WHERE cr.idAfiliado = :idAfiliado ORDER BY cr.nome, cr.corretora")
  public List<ContaResultadoView> getListContaResultado(Integer idAfiliado);

}
