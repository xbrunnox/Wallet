package br.app.grid.wallet.web.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegistrarCotacaoRequest
 * 
 * @author Brunno José Guimarães de Almeida
 * @since 25 de maio de 2023.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrarCotacaoRequest {

  private String ativo;

  private BigDecimal preco;

}
