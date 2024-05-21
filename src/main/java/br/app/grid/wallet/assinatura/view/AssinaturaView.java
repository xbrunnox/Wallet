package br.app.grid.wallet.assinatura.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AssinaturaView</b><br>
 * View para a entidade Assinatura.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Entity
@Table(name = "assinatura_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaView {

  @Id
  private Integer id;

  private Integer idAfiliado;

  private String conta;

  private String nome;

  private String corretora;

  private String maquina;

  private Integer idServidor;

  private String servidor;

  private LocalDate dataVencimento;

  private Boolean pendente;

  private String pendencia;
  
  private String observacao;

  @Transient
  private BigDecimal resultado;

}
