package br.app.grid.wallet.trade.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.app.grid.wallet.trade.vo.TradeResultadoDiaVO;

/**
 * <b>TradeResultadoDiaRepository</b><br>
 * Repository responsável pelas consultas de banco de dados da entidade TradeResultadoDiaVO.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
public interface TradeResultadoDiaRepository extends CrudRepository<TradeResultadoDiaVO, Integer> {

  @Query("FROM TradeResultadoDiaVO tr ORDER BY tr.data")
  public List<TradeResultadoDiaVO> getList();

  @Query("FROM TradeResultadoDiaVO tr WHERE tr.expert = :expert ORDER BY tr.data")
  public List<TradeResultadoDiaVO> getList(String expert);

  @Query("FROM TradeResultadoDiaVO tr WHERE tr.idAfiliado = :idAfiliado ORDER BY tr.data")
  public List<TradeResultadoDiaVO> getList(Integer idAfiliado);

}
