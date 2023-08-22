package br.app.grid.wallet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 11 de maio de 2023.
 */
public class ExcelImport {

	public static List<BacktestOperacao> importBacktestOperacoes(Backtest backtest, String nomeDoArquivo) {

		List<BacktestOperacao> retorno = new ArrayList<>();
		try {
			FileInputStream arquivo = new FileInputStream(new File(nomeDoArquivo));

			XSSFWorkbook workbook = new XSSFWorkbook(arquivo);

			XSSFSheet sheetAlunos = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheetAlunos.iterator();

			boolean blocoTransacoes = false;
			boolean leituraIniciada = false;

			BacktestOperacao operacao = null;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				String horario = null;
				String oferta = null;
				String ativo = null;
				String tipo = null;
				String direcao = null;
				String volume = null;
				String preco = null;
				String ordem = null;
				String comissao = null;
				String swap = null;
				String lucro = null;

				final int HORARIO = 0;
				final int OFERTA = 1;
				final int ATIVO = 2;
				final int TIPO = 3;
				final int DIRECAO = 4;
				final int VOLUME = 5;
				final int PRECO = 6;
				final int ORDEM = 7;
				final int COMISSAO = 8;
				final int SWAP = 9;
				final int LUCRO = 10;

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case HORARIO:
						horario = cell.getStringCellValue();
						if (horario.equals("Transações")) {
							blocoTransacoes = true;
						} else if (blocoTransacoes && horario.equals("Horário")) {
							leituraIniciada = true;
						} else if (blocoTransacoes && leituraIniciada) {
						}
						break;
					case OFERTA:
						try {
							oferta = cell.getStringCellValue();
						} catch (Exception e) {
							oferta = cell.getNumericCellValue() + "";
						}
						break;
					case ATIVO:
						ativo = cell.getStringCellValue();
						break;
					case TIPO:
						try {
							tipo = cell.getStringCellValue();
						} catch (Exception e) {
							tipo = cell.getNumericCellValue() + "";
						}
						break;
					case DIRECAO:
						direcao = cell.getStringCellValue();
						break;
					case VOLUME:
						volume = cell.getStringCellValue();
						break;
					case PRECO:
						try {
							preco = cell.getStringCellValue();
						} catch (Exception e) {
							preco = cell.getNumericCellValue() + "";
						}
						break;
					case ORDEM:
						try {
							ordem = cell.getStringCellValue();
						} catch (Exception e) {
							ordem = cell.getNumericCellValue() + "";
						}
						break;
					case COMISSAO:
						try {
							comissao = cell.getStringCellValue();
						} catch (Exception e) {
							comissao = cell.getNumericCellValue() + "";
						}
						break;
					case SWAP:
						try {
							swap = cell.getStringCellValue();
						} catch (Exception e) {
							swap = cell.getNumericCellValue() + "";
						}
						break;
					case LUCRO:
						try {
							lucro = cell.getStringCellValue();
						} catch (Exception e) {
							lucro = cell.getNumericCellValue() + "";
						}
						break;
					}
				}

				if (tipo.equals("buy") || tipo.equals("sell")) {
					if (direcao.equals("in")) {
						operacao = BacktestOperacao.builder().backtest(backtest)
								.dataEntrada(VersatilUtil.toLocalDateTime(horario, "yyyy.MM.dd HH:mm:ss"))
								.dataSaida(null).direcao(tipo.equals("buy") ? "C" : "V").duracao(0).lucro(null)
								.precoEntrada(BigDecimal.valueOf(Double.parseDouble(preco))).precoSaida(null)
								.volume(BigDecimal.valueOf(Double.parseDouble(volume))).build();
					} else if (direcao.equals("out")) {
						operacao.setDataSaida(VersatilUtil.toLocalDateTime(horario, "yyyy.MM.dd HH:mm:ss"));
						operacao.setPrecoSaida(BigDecimal.valueOf(Double.parseDouble(preco)));
						operacao.setLucro(BigDecimal.valueOf(Double.parseDouble(lucro)));
						operacao.setDuracao(
								(int) ChronoUnit.SECONDS.between(operacao.getDataEntrada(), operacao.getDataSaida()));
						retorno.add(operacao);
						operacao = null;
					}
				}
			}
			arquivo.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	public static List<BacktestOperacao> importBacktestOperacoes(Backtest backtest, InputStream inputStream) {

		List<BacktestOperacao> retorno = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

			XSSFSheet sheetAlunos = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheetAlunos.iterator();

			boolean blocoTransacoes = false;
			boolean leituraIniciada = false;

			BacktestOperacao operacao = null;

			List<BacktestOperacao> operacoesEmAndamento = new ArrayList<BacktestOperacao>();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				String horario = null;
				String oferta = null;
				String ativo = null;
				String tipo = null;
				String direcao = null;
				String volume = null;
				String preco = null;
				String ordem = null;
				String comissao = null;
				String swap = null;
				String lucro = null;

				final int HORARIO = 0;
				final int OFERTA = 1;
				final int ATIVO = 2;
				final int TIPO = 3;
				final int DIRECAO = 4;
				final int VOLUME = 5;
				final int PRECO = 6;
				final int ORDEM = 7;
				final int COMISSAO = 8;
				final int SWAP = 9;
				final int LUCRO = 10;

				System.out.println("Linha: " + row.getRowNum());

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case HORARIO:
						horario = cell.getStringCellValue();
						if (horario.equals("Transações")) {
							blocoTransacoes = true;
						} else if (blocoTransacoes && horario.equals("Horário")) {
							leituraIniciada = true;
						} else if (blocoTransacoes && leituraIniciada) {
						}
						break;
					case OFERTA:
						try {
							oferta = cell.getStringCellValue();
						} catch (Exception e) {
							oferta = cell.getNumericCellValue() + "";
						}
						break;
					case ATIVO:
						ativo = cell.getStringCellValue();
						break;
					case TIPO:
						try {
							tipo = cell.getStringCellValue();
						} catch (Exception e) {
							tipo = cell.getNumericCellValue() + "";
						}
						break;
					case DIRECAO:
						direcao = cell.getStringCellValue();
						break;
					case VOLUME:
						volume = cell.getStringCellValue();
						break;
					case PRECO:
						try {
							preco = cell.getStringCellValue();
						} catch (Exception e) {
							preco = cell.getNumericCellValue() + "";
						}
						break;
					case ORDEM:
						try {
							ordem = cell.getStringCellValue();
						} catch (Exception e) {
							ordem = cell.getNumericCellValue() + "";
						}
						break;
					case COMISSAO:
						try {
							comissao = cell.getStringCellValue();
						} catch (Exception e) {
							comissao = cell.getNumericCellValue() + "";
						}
						break;
					case SWAP:
						try {
							swap = cell.getStringCellValue();
						} catch (Exception e) {
							swap = cell.getNumericCellValue() + "";
						}
						break;
					case LUCRO:
						try {
							lucro = cell.getStringCellValue();
						} catch (Exception e) {
							lucro = cell.getNumericCellValue() + "";
						}
						break;
					}
				}
				System.out.println(horario);
				if (tipo.contains("buy") || tipo.contains("sell")) {
					if (direcao.equals("in")) {
						operacao = BacktestOperacao.builder().backtest(backtest)
								.dataEntrada(VersatilUtil.toLocalDateTime(horario, "yyyy.MM.dd HH:mm:ss"))
								.dataSaida(null).direcao(tipo.contains("buy") ? "C" : "V").duracao(0).lucro(null)
								.precoEntrada(BigDecimal.valueOf(Double.parseDouble(preco))).precoSaida(null)
								.volume(BigDecimal.valueOf(Double.parseDouble(volume))).build();
						operacoesEmAndamento.add(operacao);
					} else if (direcao.equals("out")) {
						operacao = operacoesEmAndamento.get(operacoesEmAndamento.size() - 1);
						if (operacao.getVolume().equals(BigDecimal.valueOf(Double.parseDouble(volume)))) {
							// Saída integral
							operacao.setDataSaida(VersatilUtil.toLocalDateTime(horario, "yyyy.MM.dd HH:mm:ss"));
							operacao.setPrecoSaida(BigDecimal.valueOf(Double.parseDouble(preco)));
							operacao.setLucro(BigDecimal.valueOf(Double.parseDouble(lucro)));
							operacao.setDuracao((int) ChronoUnit.SECONDS.between(operacao.getDataEntrada(),
									operacao.getDataSaida()));
							retorno.add(operacao);
							operacao = null;
							operacoesEmAndamento.remove(operacao);
						} else {
							// Saída parcial
							if (operacao.getVolume().compareTo(BigDecimal
											.valueOf(Double.parseDouble(volume))) >= 0) {
								BacktestOperacao operacaoSaida = BacktestOperacao.builder().backtest(backtest)
										.dataEntrada(operacao.getDataEntrada())
										.dataSaida(VersatilUtil.toLocalDateTime(horario, "yyyy.MM.dd HH:mm:ss"))
										.direcao(operacao.getDirecao())
										.duracao((int) ChronoUnit.SECONDS.between(operacao.getDataEntrada(),
												operacao.getDataSaida()))
										.lucro(BigDecimal.valueOf(Double.parseDouble(lucro)))
										.precoEntrada(operacao.getPrecoEntrada())
										.precoSaida((BigDecimal.valueOf(Double.parseDouble(preco)))).volume(BigDecimal
												.valueOf(Double.parseDouble(volume)).subtract(operacao.getVolume()))
										.build();
								retorno.add(operacaoSaida);
								operacao.setVolume(operacao.getVolume().subtract(operacaoSaida.getVolume()));
							}
							

						}
					}
				}
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

}
