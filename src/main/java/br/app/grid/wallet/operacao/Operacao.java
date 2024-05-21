package br.app.grid.wallet.operacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.robo.Robo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "conta")
  private Conta conta;

  @ManyToOne
  @JoinColumn(name = "expert")
  private Robo expert;

  @ManyToOne
  @JoinColumn(name = "id_afiliado")
  private Afiliado afiliado;

  @ManyToOne
  @JoinColumn(name = "ativo")
  private Ativo ativo;

  private BigDecimal volume;

  private Double preco;

  private String direcao;

  private String tipo;

  private LocalDateTime data;

}
