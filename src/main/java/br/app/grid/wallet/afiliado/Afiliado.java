package br.app.grid.wallet.afiliado;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Afiliado</b><br>
 * Entidade que representa um afiliado.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 14 de dezembro de 2023.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Afiliado implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nome;
  
  private String icone;
  
  private String logo;
  
  private String url;

  private Boolean ativo;

  private LocalDateTime dataDeCadastro;

}
