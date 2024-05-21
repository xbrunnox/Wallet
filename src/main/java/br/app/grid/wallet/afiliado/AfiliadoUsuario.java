package br.app.grid.wallet.afiliado;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import br.app.grid.wallet.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AfiliadoUsuario</b><br>
 * Entidade que representa um usuário do afiliado.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AfiliadoUsuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_afiliado")
  private Afiliado afiliado;

  @ManyToOne
  @JoinColumn(name = "id_usuario")
  private Usuario usuario;

  private LocalDateTime dataDeCadastro;

  private Boolean padrao;

}
