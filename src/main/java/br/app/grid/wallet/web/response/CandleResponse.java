package br.app.grid.wallet.web.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties
public class CandleResponse {

  private BigDecimal open;

  private BigDecimal close;

  private BigDecimal low;

  private BigDecimal high;

  private BigDecimal spread;

  private Integer volume;

  @JsonAlias(value = {"tick_volume", "tickVolume"})
  private Integer tickVolume;

  @JsonAlias(value = {"dateTime", "date_time"})
  private LocalDateTime dateTime;

}
