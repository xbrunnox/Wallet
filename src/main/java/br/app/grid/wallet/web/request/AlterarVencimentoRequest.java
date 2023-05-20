package br.app.grid.wallet.web.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarVencimentoRequest {

  private LocalDate dataVencimento;

  private LocalDate novoVencimento;

  private Integer idAssinatura;
  
  private String motivo;

}
