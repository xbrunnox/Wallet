package br.app.grid.wallet.enums;

import java.util.Objects;

public enum DirecaoOperacaoEnum {

	BUY, SELL;

	public static DirecaoOperacaoEnum fromString(String string) {
		if (Objects.isNull(string))
			return null;
		String stringLowerCase = string.toLowerCase();
		if (stringLowerCase.equals("buy") || stringLowerCase.equals("compra"))
			return BUY;
		if (stringLowerCase.equals("sell") || stringLowerCase.equals("venda"))
			return SELL;
		return null;
	}
}