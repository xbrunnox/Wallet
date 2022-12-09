package br.app.grid.wallet.grafico;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SerieInt {
	
	private String name;
	
	private List<String> categorias;
	
	private List<Integer> data;
	
	public SerieInt() {
		categorias = new ArrayList<>();
		data = new ArrayList<>();
	}
	
	public void addCategoria(String label) {
		if (categorias == null)
			categorias = new ArrayList<>();
		categorias.add(label);
	}
	
	public void addData(Integer valor) {
		if (data == null)
			data = new ArrayList<>();
		data.add(valor);
	}

}
