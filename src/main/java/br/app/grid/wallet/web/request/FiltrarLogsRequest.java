package br.app.grid.wallet.web.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>FiltrarLogsRequest</b><br>
 * Request responsável pelo filtro de logs.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 28 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltrarLogsRequest {

  private LocalDate dataInicial;
  private LocalDate dataFinal;

  private String conta1;
  private String conta2;
  private String conteudo;

}
