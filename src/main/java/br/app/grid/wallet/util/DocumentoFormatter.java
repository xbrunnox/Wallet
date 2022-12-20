package br.app.grid.wallet.util;

public class DocumentoFormatter {

	public static String format(String cpfCnpj) {
		if (cpfCnpj == null)
			return null;
		if (cpfCnpj.length() == 11) {
			return cpfCnpj.substring(0, 3) + "." + cpfCnpj.substring(3, 6) + "." + cpfCnpj.substring(6, 9) + "-"
					+ cpfCnpj.substring(9, 11);
		}
		return cpfCnpj;
	}
	
	public static void main(String[] args) {
		System.out.println(format("04274114406"));
	}

}
