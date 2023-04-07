package br.app.grid.wallet.util.converter;

import java.util.ArrayList;
import java.util.List;

import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.web.response.OperacaoResponse;

public class OperacaoResponseConverter {

	public static List<OperacaoResponse> converter(List<Operacao> operacoes) {
		List<OperacaoResponse> retorno = new ArrayList<>();
		for (Operacao operacao : operacoes) {
			retorno.add(converter(operacao));
		}
		return retorno;
	}

	public static OperacaoResponse converter(Operacao operacao) {
		return OperacaoResponse.builder().ativo(operacao.getAtivo().getCodigo())
				.categoria(operacao.getAtivo().getCategoria().getNome()).conta(operacao.getConta().getId())
				.data(operacao.getData()).direcao(operacao.getDirecao())
				.expert((operacao.getExpert() != null ? operacao.getExpert().getId() : null)).id(operacao.getId())
				.nome(operacao.getConta().getNome()).volume(operacao.getVolume()).preco(operacao.getPreco()).build();
	}

}
