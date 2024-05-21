package br.app.grid.wallet.robo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import br.app.grid.wallet.afiliado.Afiliado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * <b>Robo</b><br>
 * Entidade que representa uma automação.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
public class Robo {

  @Id
  private String id;

  private String ativo;

  @ManyToOne
  @JoinColumn(name = "id_afiliado")
  private Afiliado afiliado;

  private String descricao;

  private Double volume;

  private Double alvo;

  private Double stop;

  private Double tolerancia;

  private Double minimo;

  private int tentativas;

  private int timeframe;

  private Boolean enabled;

}
