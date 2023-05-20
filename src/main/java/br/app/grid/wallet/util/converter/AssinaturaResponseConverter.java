package br.app.grid.wallet.util.converter;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.web.response.AssinaturaResponse;

/**
 * <b>AssinaturaResponseConverter</b><br>
 * Classe utilitária para conversão de AssinaturaResponse.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de abril de 2023.
 */
public class AssinaturaResponseConverter {
  
  public static AssinaturaResponse convert(Assinatura assinatura) {
    return AssinaturaResponse.builder()
        .conta(assinatura.getConta().getId())
        .corretora(assinatura.getConta().getCorretora())
        .dataVencimento(assinatura.getDataVencimento())
        .email(assinatura.getEmailPagamento())
        .id(assinatura.getId())
        .nome(assinatura.getConta().getNome())
        .telefone(assinatura.getTelefone())
        .build();
  }

}
