package br.app.grid.wallet.dividendo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.ativo.AtivoRepository;
import br.app.grid.wallet.fii.FiiDividendo;
import br.app.grid.wallet.util.FiiUtil;

@Service
public class DividendoService {

	@Autowired
	private AtivoRepository ativoRepository;

	@Autowired
	private DividendoRepository dividendoRepository;

	public Dividendo getDividendo(String codigoAtivo) {
		Ativo ativo = ativoRepository.findByCodigo(codigoAtivo);
		if (ativo == null) {
			FiiDividendo fiiDividendo = FiiUtil.getDividendo(codigoAtivo);
			if (fiiDividendo != null) {
				ativo = new Ativo();
				ativo.setCodigo(codigoAtivo.toUpperCase());
//				ativo.setNome("");
				ativoRepository.save(ativo);
			} else {
				return null;
			}
			Dividendo dividendo = Dividendo.builder().ativo(ativo).dataBase(fiiDividendo.getDataBase())
					.dataPagamento(fiiDividendo.getDataPagamento()).valor(fiiDividendo.getValor())
					.yield(fiiDividendo.getYield()).dataCadastro(LocalDateTime.now()).build();
			dividendoRepository.save(dividendo);
			return dividendo;
		} else {
			LocalDate dataAtual = LocalDate.now();
			Dividendo ultimoDividendo = getUltimoDividendo(ativo);
			if (ultimoDividendo == null || ultimoDividendo.getDataBase().compareTo(dataAtual) < 0) {
				FiiDividendo fiiDividendo = FiiUtil.getDividendo(codigoAtivo);
				if (fiiDividendo == null)
					return null;
				if (ultimoDividendo == null || !fiiDividendo.getDataBase().equals(ultimoDividendo.getDataBase())) {
					Dividendo dividendo = Dividendo.builder().ativo(ativo).dataBase(fiiDividendo.getDataBase())
							.dataPagamento(fiiDividendo.getDataPagamento()).valor(fiiDividendo.getValor())
							.yield(fiiDividendo.getYield()).dataCadastro(LocalDateTime.now()).build();
					dividendoRepository.save(dividendo);
					ultimoDividendo = dividendo;
				}
			}
			return ultimoDividendo;
		}
	}

	private Dividendo getUltimoDividendo(Ativo ativo) {
		List<Dividendo> dividendos = dividendoRepository.getUltimoDividendo(ativo);
		if (dividendos.size() > 0)
			return dividendos.get(0);
		return null;
	}

}
