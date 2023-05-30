package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 25 de maio de 2023.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CotacaoResponse {
  
  private String ativo;
  
  private BigDecimal preco;
  
  private LocalDateTime data;

}
