package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AssociarTratarPagamentoRequest</b><br>
 * Request para associação e tratamento de pagamento.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociarTratarPagamentoRequest {

  private Integer idAssinatura;
  private Long idPagamento;
  private Boolean apenasAssociar;

}
