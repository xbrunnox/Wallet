package br.app.grid.wallet.backtest.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.backtest.repository.BacktestOperacaoRepository;
import br.app.grid.wallet.backtest.repository.BacktestRepository;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoResultadoDiaVO;
import br.app.grid.wallet.backtest.vo.BacktestOperacaoResultadoMesVO;

@Service
public class BacktestService {

	@Autowired
	private BacktestRepository repository;

	@Autowired
	private BacktestOperacaoRepository operacaoRepository;

	public Backtest get(Integer id) {
		return repository.findById(id).orElse(null);
	}

	public void importar(Integer idBacktest, String nomeDoArquivo) {
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo));
			String linha = br.readLine();
			while (linha != null) {
				String dados[] = linha.split("\t");
				System.out.println(new ObjectMapper().writeValueAsString(dados));
				if (dados[4].equals("in")) {
					// Entrada
					LocalDateTime dataEntrada = LocalDateTime.parse(dados[0], formatador);
					String direcao = (dados[3].equals("buy") ? "C" : "V");
					BigDecimal volume = BigDecimal.valueOf(Double.parseDouble(dados[5]));
					BigDecimal precoEntrada = BigDecimal
							.valueOf(Double.parseDouble(dados[6].replace(" ", "").replace(",", ".")));
					linha = br.readLine();
					dados = linha.split("\t");
					if (dados[4].equals("out")) {
						// Saída
						LocalDateTime dataSaida = LocalDateTime.parse(dados[0], formatador);
						BigDecimal precoSaida = BigDecimal
								.valueOf(Double.parseDouble(dados[6].replace(" ", "").replace(",", ".")));
						BigDecimal lucro = BigDecimal
								.valueOf(Double.parseDouble(dados[10].replace(" ", "").replace(",", ".")));
						Backtest backtest = get(idBacktest);

						BacktestOperacao backOperacao = BacktestOperacao.builder().dataEntrada(dataEntrada).lucro(lucro)
								.volume(volume).duracao((int) ChronoUnit.SECONDS.between(dataEntrada, dataSaida))
								.dataSaida(dataSaida).precoEntrada(precoEntrada).precoSaida(precoSaida).direcao(direcao)
								.backtest(backtest).build();
						operacaoRepository.save(backOperacao);
						System.out.println(new ObjectMapper().writeValueAsString(backOperacao));
					}

				}

				linha = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importar2(Integer idBacktest, String transacoes) {
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		Scanner scanner = new Scanner(transacoes);
		String linha = scanner.nextLine();
		while (linha != null) {
			String dados[] = linha.split("\t");
			if (dados[4].equals("in")) {
				// Entrada
				LocalDateTime dataEntrada = LocalDateTime.parse(dados[0], formatador);
				String direcao = (dados[3].equals("buy") ? "C" : "V");
				BigDecimal volume = BigDecimal.valueOf(Double.parseDouble(dados[5]));
				BigDecimal precoEntrada = BigDecimal
						.valueOf(Double.parseDouble(dados[6].replace(" ", "").replace(",", ".")));
				if (scanner.hasNextLine()) {
					linha = scanner.nextLine();
				} else {
					break;
				}
				dados = linha.split("\t");
				if (dados[4].equals("out")) {
				    System.out.println("Lucro: "+dados[11]);
					// Saída
					LocalDateTime dataSaida = LocalDateTime.parse(dados[0], formatador);
					BigDecimal precoSaida = BigDecimal
							.valueOf(Double.parseDouble(dados[6].replace(" ", "").replace(",", ".").replace("--", "-")));
					BigDecimal lucro = BigDecimal
							.valueOf(Double.parseDouble(dados[11].replace(" ", "").replace(",", ".").replace("--", "-")));
					Backtest backtest = get(idBacktest);

					BacktestOperacao backOperacao = BacktestOperacao.builder().dataEntrada(dataEntrada).lucro(lucro)
							.volume(volume).duracao((int) ChronoUnit.SECONDS.between(dataEntrada, dataSaida))
							.dataSaida(dataSaida).precoEntrada(precoEntrada).precoSaida(precoSaida).direcao(direcao)
							.backtest(backtest).build();
					operacaoRepository.save(backOperacao);
				}

			}
			if (scanner.hasNextLine()) {
				linha = scanner.nextLine();
			} else {
				break;
			}
		}
		scanner.close();
	}

	public List<Backtest> getList() {
		return repository.getList();
	}

  public List<BacktestOperacaoResultadoDiaVO> getListResultadosDiarios(Integer idBacktest) {
    return operacaoRepository.getListResultadosDiarios(idBacktest);
  }

  public List<BacktestOperacaoResultadoMesVO> getListResultadosMensais(Integer idBacktest) {
    return operacaoRepository.getListResultadosMensais(idBacktest);
  }

}
