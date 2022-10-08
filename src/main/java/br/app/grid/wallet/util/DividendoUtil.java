package br.app.grid.wallet.util;

public class DividendoUtil {

	public static void main(String[] args) {
		String pagina = HttpUtil.get("https://mundofii.com/fundos/IRDM11");
		System.out.println(pagina);
	}
	
}
