package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 23 de janeiro de 2023.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtivarAssinaturaRequest {

  private String idConta;

  private String idExpert;

  private Long idPagamento;

}
