package br.app.grid.wallet.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AdicionarExpertAssinaturaRequest</b><br>
 * Request para adição de um expert na assinatura.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 16 de setembro de 2023.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdicionarExpertAssinaturaRequest {
  
  private Integer idAssinatura;
  
  private String expert;
  
  private Integer alavancagem;

  private Integer alavancagemMaxima;

}
