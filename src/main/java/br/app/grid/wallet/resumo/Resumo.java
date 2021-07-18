package br.app.grid.wallet.resumo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resumo {

	private int idCarteira;

	private String nome;

	private BigDecimal saldo;

	private List<ResumoPosicao> posicoes;

	private BigDecimal total;

	public void add(String codigo, int quantidade, BigDecimal valor, BigDecimal strike) {
		if (posicoes == null)
			posicoes = new ArrayList<>();
		posicoes.add(ResumoPosicao.builder().ativo(codigo).quantidade(quantidade).strike(strike).valor(valor).build());
	}

	public void atualizarTotais() {
		total = BigDecimal.valueOf(0);
		for (ResumoPosicao posicao : posicoes) {
			if (posicao.getStrike() == null) {
				posicao.setTotal(posicao.getValor().multiply(BigDecimal.valueOf(posicao.getQuantidade())).setScale(2));
			} else if (posicao.getValor().compareTo(posicao.getStrike()) > 0)
				posicao.setTotal(posicao.getStrike().multiply(BigDecimal.valueOf(posicao.getQuantidade())).setScale(2));
			else
				posicao.setTotal(posicao.getValor().multiply(BigDecimal.valueOf(posicao.getQuantidade())).setScale(2));
			total = total.add(posicao.getTotal()).setScale(2);
		}
		for (ResumoPosicao posicao : posicoes) {
			posicao.setPercentual(
					posicao.getTotal().multiply(BigDecimal.valueOf(100)).divide(total, 1, RoundingMode.HALF_UP));
		}
		total = total.add(saldo).setScale(2);
	}

}
