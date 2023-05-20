package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AlterarEmailRequest</b><br>
 * Request para alteração de email de uma assinatura.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarEmailRequest {

  private Integer idAssinatura;

  private String email;

  private String novoEmail;

}
