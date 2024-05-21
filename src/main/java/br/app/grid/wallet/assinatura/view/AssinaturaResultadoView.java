package br.app.grid.wallet.assinatura.view;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AssinaturaResultadoView</b><br>
 * View para a entidade Assinatura com os resultados de trade.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Entity
@Table(name = "assinatura_resultado_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaResultadoView {
  
  @Id
  private Integer id;
  
  private Integer idAfiliado;
  
  private String conta;
  
  private BigDecimal resultado;

}
