package br.app.grid.wallet.util.converter;

import java.math.BigDecimal;
import java.util.Objects;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.web.response.AssinaturaPagamentoResponse;

/**
 * <b>AssinaturaPagamentoResponseConverter</b><br>
 * Classe utilitária para realizar as conversões relativas a entidade AssinaturaPagamentoResponse.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
public class AssinaturaPagamentoResponseConverter {

  /**
   * Realiza a conversão de AssinaturaPagamento para AssinaturaPagamentoResponse.
   * @param assinaturaPagamento AssinaturaPagamento a ser convertida.
   * @return Resultado da conversão.
   */
  public static AssinaturaPagamentoResponse convert(AssinaturaPagamento assinaturaPagamento) {
    if (Objects.isNull(assinaturaPagamento))
      return null;
    Pagamento pagamento = assinaturaPagamento.getPagamento();
    return AssinaturaPagamentoResponse.builder().data(pagamento.getDataAtualizacao())
        .email(pagamento.getEmail()).id(assinaturaPagamento.getId()).nome(pagamento.getNome())
        .plataforma(pagamento.getPlataforma()).produto(pagamento.getProduto())
        .telefone(pagamento.getTelefone()).valor(BigDecimal.valueOf(pagamento.getValor())).build();
  }

}
