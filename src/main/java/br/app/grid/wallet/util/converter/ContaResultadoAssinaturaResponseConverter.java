package br.app.grid.wallet.util.converter;

import java.util.ArrayList;
import java.util.List;

import br.app.grid.wallet.licenca.ContaResultadoView;
import br.app.grid.wallet.web.response.ContaResultadoAssinaturaResponse;

public class ContaResultadoAssinaturaResponseConverter {

	public static List<ContaResultadoAssinaturaResponse> converter(List<ContaResultadoView> contas) {
		List<ContaResultadoAssinaturaResponse> retorno = new ArrayList<>();
		for (ContaResultadoView conta : contas) {
			retorno.add(converter(conta));
		}
		return retorno;
	}

	public static ContaResultadoAssinaturaResponse converter(ContaResultadoView conta) {
		return ContaResultadoAssinaturaResponse.builder().corretora(conta.getCorretora()).id(conta.getId())
				.nome(conta.getNome()).resultado(conta.getResultado()).build();
	}

}
