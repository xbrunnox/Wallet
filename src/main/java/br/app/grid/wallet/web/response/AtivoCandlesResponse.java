package br.app.grid.wallet.web.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>AtivoCandlesResponse</b>
 * @author Brunno José Guimarães de Almeida.
 * @since 17 de maio de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class AtivoCandlesResponse {
  
  private String ativo;
  
  @JsonAlias(value = {"timeFrame","time_frame"})
  private Integer timeFrame;
  
  private List<CandleResponse> candles;

}
