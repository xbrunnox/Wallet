package br.app.grid.wallet.web.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AssinaturaResponse</b><br>
 * Response para a entidade Assinatura.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de abril de 2023.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaResponse {

  private Integer id;

  private String nome;
  private String corretora;
  private String conta;
  private String email;
  private String telefone;

  private LocalDate dataVencimento;

}
