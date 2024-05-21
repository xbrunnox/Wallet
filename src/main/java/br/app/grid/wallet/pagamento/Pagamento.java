package br.app.grid.wallet.pagamento;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import br.app.grid.wallet.afiliado.Afiliado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Pagamento</b><br>
 * Entidade que representa uma pagamento.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="id_afiliado")
  private Afiliado afiliado;

  private String nome; // : "Mario Chase",
  private String email; // : "test@example.com",
  private String telefone; // : null,
  private String cpf; // : null,

  private String idPedido; // "da292c35-c6fc-44e7-ad19-ff7865bc2d89",
  private String refPedido; // "Quzqwus",
  private String status; // "paid", //Status da venda
  private String formaDePagamento; // "credit_card",
  private String idLoja; // "JKzixndUxOr68LJ",
  private String idCliente; // "10869585",
  private LocalDateTime dataCriacao; // ": "2020-12-21 10:46",
  private LocalDateTime dataAtualizacao; // : "2020-12-21 10:46",


  private String idProduto; // : "acfe6050-4387-11eb-85a0-43a3ebec8277",
  private String produto; // ": "New Subscription"

  private Double valor;
  private Double taxas;

  private String ip; // : "192.168.0.1"

  private String plataforma;

  private boolean associado;


}
