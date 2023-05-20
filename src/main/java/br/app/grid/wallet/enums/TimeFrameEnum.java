package br.app.grid.wallet.enums;

import lombok.Getter;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 12 de maio de 2023.
 */
public enum TimeFrameEnum {

  M1(1), M2(2), M3(3), M4(4), M5(5), M6(6), M10(10), M12(12), M15(15), M20(20), M30(30), H1(
      16385), H2(16386), H3(16387), H4(16388), D1(16408);

  @Getter
  private Integer valor;

  TimeFrameEnum(Integer valor) {
    this.valor = valor;
  }


}
