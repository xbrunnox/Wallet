package br.app.grid.wallet.web.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandlesRequest {
  
  private String ativo;
  
  private Integer timeFrame;
  
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dataInicial;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dataFinal;

}
