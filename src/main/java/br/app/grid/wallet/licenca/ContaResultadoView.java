package br.app.grid.wallet.licenca;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>ContaResultadoView</b><br>
 * Entidade que representa os trades realizados de uma conta.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ContaResultadoView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Id
  private Integer idAfiliado;

  private String nome;
  private String corretora;
  private BigDecimal resultado;

}
