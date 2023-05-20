package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>ExcluirAssinaturaPagamentoRequest</b><br>
 * Request para exclusão do pagamento com o ID indicado.
 * @author Brunno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcluirAssinaturaPagamentoRequest {
  
  private Integer id;
  
  private Integer idAssinatura;

}