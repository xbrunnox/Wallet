package br.app.grid.wallet.afiliado;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AfiliadoLoja</b><br>
 * Entidade que identifica o afiliado ao qual pertence a loja.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 03 de março de 2024.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AfiliadoLoja {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String idLoja;

  @ManyToOne
  @JoinColumn(name = "id_afiliado")
  private Afiliado afiliado;

  private LocalDateTime dataDeCadastro;

}
