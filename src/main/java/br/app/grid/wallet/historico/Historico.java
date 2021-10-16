package br.app.grid.wallet.historico;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.app.grid.wallet.aporte.Aporte;
import br.app.grid.wallet.patrimonio.Patrimonio;
import lombok.Data;

@Data
public class Historico {

	private List<HistoricoItem> itens;

	@JsonIgnore
	private Map<Integer, HistoricoItem> mapaItens;

	private BigDecimal saldo;

	private BigDecimal aporteMensal;

	private BigDecimal rendimentoEsperado;

	public Historico() {
		itens = new ArrayList<>();
		mapaItens = new HashMap<>();
	}

	public void add(List<Aporte> aportes) {
		for (Aporte aporte : aportes) {
			HistoricoItem item = getItem(aporte.getData().getMonthValue(), aporte.getData().getYear());
			item.addAporte(aporte.getValor());
		}
	}

	public void addPatrimonios(List<Patrimonio> patrimonios) {
		for (Patrimonio patrimonio : patrimonios) {
			HistoricoItem item = getItem(patrimonio.getData().getMonthValue(), patrimonio.getData().getYear());
			item.setRealizado(patrimonio.getValor());
		}
	}

	private HistoricoItem getItem(int mes, int ano) {
		int chave = mes * 10000 + ano;
		HistoricoItem item = mapaItens.get(chave);
		if (item == null) {
			item = new HistoricoItem();
			item.setMes(mes);
			item.setAno(ano);
			itens.add(item);
			mapaItens.put(chave, item);
		}
		return item;
	}

	public void atualizar() {
		BigDecimal saldoAnterior = new BigDecimal(0).setScale(2);
		BigDecimal previstoAnterior = new BigDecimal(0).setScale(2);
		for (HistoricoItem item : itens) {
			BigDecimal saldoBase = null;
			if (saldoAnterior.compareTo(previstoAnterior) > 0) {
				saldoBase = saldoAnterior;
			} else {
				saldoBase = previstoAnterior;
			}
			BigDecimal aporteDoMes = null;
			if (item.getAporte() != null && item.getAporte().compareTo(aporteMensal) > 0) {
				aporteDoMes = item.getAporte();
			} else {
				aporteDoMes = aporteMensal;
			}

			BigDecimal baseDeCalculo = saldoBase.add(aporteDoMes);
			BigDecimal valorPrevisto = baseDeCalculo
					.add(baseDeCalculo.multiply(rendimentoEsperado).divide(new BigDecimal(100)));

			BigDecimal rendimento = item.getRealizado()
					.subtract(saldoAnterior.add((item.getAporte() != null ? item.getAporte() : new BigDecimal(0))));

			BigDecimal rendimentoPercentual = new BigDecimal(0);
			if (baseDeCalculo.compareTo(new BigDecimal(0)) != 0) {
				System.out.println("----");
				System.out.println(rendimento);
				System.out.println(baseDeCalculo);
				rendimentoPercentual = rendimento.multiply(new BigDecimal(100)).divide(baseDeCalculo, 2,
						RoundingMode.HALF_UP);
			}

			item.setBaseDeCalculo(baseDeCalculo);
			item.setPrevisto(valorPrevisto);
			item.setRendimento(rendimento);
			item.setRendimentoPercentual(rendimentoPercentual);

			saldoAnterior = item.getRealizado();
		}

	}
}
