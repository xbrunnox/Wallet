package br.app.grid.wallet.util;

import java.util.HashMap;
import java.util.Map;

public class CorretoraUtil {
	
	private static Map<String,String> mapaCorretoras;
	
	static {
		mapaCorretoras = new HashMap<>();
		mapaCorretoras.put("Clear (XP Investimentos CCTVM)", "Clear");
		mapaCorretoras.put("XP Investimentos CCTVM S/A", "XP");
		mapaCorretoras.put("Banco BTG Pactual S.A.", "BTG");
		mapaCorretoras.put("Genial Investimentos Corretora de Valores Mobiliários S.A.", "Genial");
		mapaCorretoras.put("Rico (XP Investimentos CCTVM)", "Rico");
		mapaCorretoras.put("Modal DTVM Ltda.", "Modal");
		mapaCorretoras.put("Orama Distribuidora de Titulos e Valores Mobiliarios SA", "Órama");
	}
	
	public static String format(String nomeCorretora) {
		if (nomeCorretora == null)
			return null;
		String corretora = mapaCorretoras.get(nomeCorretora);
		if (corretora != null)
			return corretora;
		return nomeCorretora;
	}
}
