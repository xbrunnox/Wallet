package br.app.grid.wallet.util;

public class TelefoneFormatter {

	public static String format(String telefone) {
		if (telefone == null)
			return null;
		if (telefone.startsWith("+55") && telefone.length() == 14) {
			return "(" + telefone.substring(3, 5) + ") " + telefone.substring(5, 10) + "-" + telefone.substring(10, 14);
		}
		return telefone;
	}
	
	public static void main(String[] args) {
		System.out.println(format("+5583987606205"));
	}

}
