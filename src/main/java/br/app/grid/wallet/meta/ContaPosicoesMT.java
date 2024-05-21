package br.app.grid.wallet.meta;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>ContaPosicoesMT</b><br>
 * Entidade que representa os ativos posicionados de uma conta.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaPosicoesMT {

  private String conta;

  private String nome;

  private String corretora;

  private String server;

  private Integer idAfiliado;

  private List<PosicaoMT> posicoes;

}
