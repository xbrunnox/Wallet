package br.app.grid.wallet.web.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegistrarRsiRequest
 * 
 * @author Brunno José Guimarães de Almeida
 * @since 30 de maio de 2023.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrarRsiRequest {

  private String ativo;

  private BigDecimal rsi;

}
