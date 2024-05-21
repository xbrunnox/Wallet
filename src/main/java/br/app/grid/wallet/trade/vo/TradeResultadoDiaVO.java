package br.app.grid.wallet.trade.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "trade_resultado_dia_view")
@NoArgsConstructor
@AllArgsConstructor
public class TradeResultadoDiaVO {

  @Id
  private Integer fakeId;

  private Integer idAfiliado;

  private LocalDate data;

  private String expert;

  private BigDecimal resultado;

}
