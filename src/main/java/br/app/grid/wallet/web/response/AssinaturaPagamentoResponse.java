package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AssinaturaPagamentoResponse</b><br>
 * Entidade que representa a entidade AssinaturaPagamento.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaPagamentoResponse {

  private Integer id;

  private LocalDateTime data;

  private String email;
  private String nome;
  private String plataforma;
  private String produto;
  private String telefone;

  private BigDecimal valor;

}
