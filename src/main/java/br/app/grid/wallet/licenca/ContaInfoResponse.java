package br.app.grid.wallet.licenca;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaInfoResponse {

  private Integer idAfiliado;
  
  private Integer idAssinatura;

  private String conta;

  private String nome;

  private String corretora;

  private Boolean ativo;

  private String expiracao;

  private Boolean pausado;

  private String maquina;

}