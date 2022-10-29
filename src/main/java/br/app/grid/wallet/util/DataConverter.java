package br.app.grid.wallet.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataConverter {
	
	private static DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public static String formatarDateTimeBR(LocalDateTime localDateTime) {
		return formatador.format(localDateTime);
	}

	public static String nowBr() {
		return formatador.format(LocalDateTime.now());
	}

}
