package br.app.grid.wallet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import br.app.grid.wallet.fii.FiiDividendo;

public class FiiUtil {

	public static List<FiiDividendo> getHistorico(String ativo) {
		ativo = ativo.toUpperCase();
		String csv = get("https://statusinvest.com.br/fundos-imobiliarios/" + ativo);
		Scanner sc = new Scanner(csv);
		boolean imprimir = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		List<FiiDividendo> retorno = new ArrayList<>();
		List<String> campos = new ArrayList<>();
		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
			if (linha.contains("Tipo do provento")) {
				imprimir = true;
			}
			if (imprimir) {
				if (linha.contains("</tbody>"))
					break;
				if (linha.contains("</tr>")) {
					if (campos.size() == 4) {
						String saida = "{";
						saida += "\"dataCom\"=\"" + campos.get(1) + "\",";
						saida += "\"dataPagamento\"=\"" + campos.get(2) + "\",";
						saida += "\"dividendo\"=\"" + campos.get(3) + "\"}";
						System.out.println(saida);
						retorno.add(FiiDividendo.builder().dataBase(LocalDate.parse(campos.get(1), formatter))
								.dataPagamento(LocalDate.parse(campos.get(2), formatter))
								.valor(BigDecimal.valueOf(Double.parseDouble(campos.get(3).replace(",", "."))))
								.build());
						campos.clear();
					}
				}
				if (linha.contains("<td")) {
					String leitura = linha;
					while (!linha.contains("</td>")) {
						linha = sc.nextLine();
						leitura += linha;
					}
					int inicio = leitura.indexOf(">") + 1;
					int fim = 0;
					leitura = leitura.replace("<div>", "").replace("</div>", "");
					if (leitura.contains("<i"))
						fim = leitura.indexOf("<i");
					else if (leitura.contains("</td>"))
						fim = leitura.indexOf("</td>");
					else {
						System.out.println(leitura);
						fim = leitura.length();
					}
					campos.add(leitura.substring(inicio, fim) + "");
				}
			}
		}
		sc.close();
		return retorno;
	}

	public static String get(String url) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			int CONNECTION_TIMEOUT_MS = 15000; // Timeout in millis.

			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
					.setConnectTimeout(CONNECTION_TIMEOUT_MS).setSocketTimeout(CONNECTION_TIMEOUT_MS).build();

			// HttpPost httpPost = new HttpPost(URL);
			// httpPost.setConfig(requestConfig);

			// use httpClient (no need to close it explicitly)
			HttpGet getRequest = new HttpGet(url);
//			System.out.println(url);
			getRequest.setConfig(requestConfig);
			getRequest.addHeader("accept", "application/x-www-form-urlencoded");
			// getRequest.addHeader("charset", "UTF-8");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				// System.out.println("Failed : HTTP error code : " +
				// response.getStatusLine().getStatusCode());
				// return null;
			}
			// response.setCharacterEncoding("UTF-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String saida = "";
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				saida += output + "\n";
			}

			// httpClient.getConnectionManager().shutdown();
			return saida;

		} catch (IOException e) {
			System.out.println(e.getMessage());
			// handle

		}
		return null;

		// try {
		// DefaultHttpClient httpClient = new DefaultHttpClient();
		// HttpGet getRequest = new HttpGet(url);
		// getRequest.addHeader("accept", "application/json");
		//
		// HttpResponse response = httpClient.execute(getRequest);
		//
		// if (response.getStatusLine().getStatusCode() != 200) {
		// throw new RuntimeException("Failed : HTTP error code : " +
		// response.getStatusLine().getStatusCode());
		// }
		//
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader((response.getEntity().getContent())));
		//
		// String output;
		// String saida = "";
		// System.out.println("Output from Server .... \n");
		// while ((output = br.readLine()) != null) {
		// saida += output;
		// }
		//
		// httpClient.getConnectionManager().shutdown();
		// return saida;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
	}

	public static String getXml(String url) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "text/xml");

			HttpResponse response = httpClient.execute(getRequest);
			// System.out.println(response.getAllHeaders());
			// for (Header hearder : response.getAllHeaders()) {
			// System.out.println(hearder.getName() + " - " +
			// hearder.getValue());
			// }

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String saida = "";
			while ((output = br.readLine()) != null) {
				saida += output;
			}

			httpClient.getConnectionManager().shutdown();
			return saida;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void postJson(String url, String json) {
		System.out.println(url);
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// for (String chave : campos.keySet()) {
			// // httpPost.setEntity(new StringEntity(chave, "UTF-8"));
			// nvps.add(new BasicNameValuePair(chave, campos.get(chave)));
			// }
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// Url Encoding the POST parameters
			// try {
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// } catch (UnsupportedEncodingException e) {
			// // writing error to Log
			// e.printStackTrace();
			// }
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps, "ISO-8859-3"));
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			if (response2.getStatusLine().getStatusCode() == 200) {
				System.out.println("OK");
				response2.close();
			} else {
				try {
					System.out.println(response2.getStatusLine());

					HttpEntity entity2 = response2.getEntity();
					// do something useful with the response body
					// and ensure it is fully consumed
					EntityUtils.consume(entity2);
				} finally {
					response2.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void post(String url, Map<String, String> campos) {
		System.out.println(url);
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String chave : campos.keySet()) {
				// httpPost.setEntity(new StringEntity(chave, "UTF-8"));
				nvps.add(new BasicNameValuePair(chave, campos.get(chave)));
			}
			// Url Encoding the POST parameters
			// try {
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// } catch (UnsupportedEncodingException e) {
			// // writing error to Log
			// e.printStackTrace();
			// }
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps, "ISO-8859-3"));
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			if (response2.getStatusLine().getStatusCode() == 200) {
				System.out.println("OK");
				response2.close();
			} else {
				try {
					System.out.println(response2.getStatusLine());

					HttpEntity entity2 = response2.getEntity();
					// do something useful with the response body
					// and ensure it is fully consumed
					EntityUtils.consume(entity2);
				} finally {
					response2.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FiiDividendo getDividendo(String ativo) {
		String csv = get("https://mundofii.com/fundos/" + ativo.toUpperCase());
		Scanner sc = new Scanner(csv);
		String saida = "";
		boolean achouUltimoRendimento = true;
		boolean coletando = false;
		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
			if (linha.contains("id=\"ULTIMO_RENDIMENTO\"")) {
				achouUltimoRendimento = true;
			} else if (achouUltimoRendimento && linha.contains("RENDIMENTO_52")) {
				achouUltimoRendimento = false;
				break;
			} else if (achouUltimoRendimento && coletando) {
				saida += linha + "\n";
			} else if (achouUltimoRendimento && linha.contains("<table class=\"margin10\">")) {
				coletando = true;
			} else if (coletando && achouUltimoRendimento && linha.contains("</table")) {
				coletando = false;
			}
		}
		sc.close();
//		System.out.println("------------");
//		System.out.println(saida);
		FiiDividendo dividendo = new FiiDividendo();
		dividendo.setAtivo(ativo);
		Scanner scSaida = new Scanner(saida);
		while (scSaida.hasNextLine()) {
			String linha = scSaida.nextLine();
			if (linha.contains("menor")) {
				String valorString = getValorDoDividendo(linha);
				if (valorString != null && valorString.length() > 0) {
					dividendo.setValor(BigDecimal.valueOf(Double.valueOf(valorString.replace(",", "."))));
				}
			} else if (linha.contains("t500")) {
				String dataString = getData(linha);
				if (dataString != null && dataString.length() > 0) {
					LocalDate data = toData(dataString);
					if (dividendo.getDataBase() == null)
						dividendo.setDataBase(data);
					else if (dividendo.getDataPagamento() == null)
						dividendo.setDataPagamento(data);
				}
				String yieldString = getYield(linha);
				if (yieldString != null) {
					dividendo.setYield(new BigDecimal(yieldString.replace(",", ".").replace("%", "")));
				}
			}
		}
		scSaida.close();
//		System.out.println("------------");
		if (dividendo.getDataBase() == null && dividendo.getValor() == null)
			return null;
		return dividendo;
	}

	private static String getYield(String linha) {
		Pattern pattern = Pattern.compile("[0-9]+,[0-9]+%");
		String mydata = linha;

		Matcher matcher = pattern.matcher(mydata);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	private static LocalDate toData(String dataString) {
//		System.out.println("Data String: "+dataString);
		LocalDate dataAtual = LocalDate.now();
		Map<String, Integer> mapaData = new HashMap<>();
		mapaData.put("jan", 1);
		mapaData.put("fev", 2);
		mapaData.put("mar", 3);
		mapaData.put("abr", 4);
		mapaData.put("mai", 5);
		mapaData.put("jun", 6);
		mapaData.put("jul", 7);
		mapaData.put("ago", 8);
		mapaData.put("set", 9);
		mapaData.put("out", 10);
		mapaData.put("nov", 11);
		mapaData.put("dez", 12);
		return LocalDate.of(dataAtual.getYear(), mapaData.get(dataString.substring(3).toLowerCase()),
				Integer.parseInt(dataString.substring(0, 2)));
	}

	public static String getValorDoDividendo(String linha) {
//		System.out.println(linha);
		Pattern pattern = Pattern.compile("[0-9]+,[0-9]+");
		String mydata = linha;

		Matcher matcher = pattern.matcher(mydata);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public static String getData(String linha) {
//		System.out.println(linha);
		Pattern pattern = Pattern.compile("[0-9]+/[a-zA-Z]+");
		String mydata = linha;

		Matcher matcher = pattern.matcher(mydata);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getDividendo("IRDM11"));
		System.out.println(getDividendo("MXRF11"));
		System.out.println(getDividendo("HCTR11"));
	}

}
