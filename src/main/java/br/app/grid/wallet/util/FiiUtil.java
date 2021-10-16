package br.app.grid.wallet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
		String csv = get("https://statusinvest.com.br/fundos-imobiliarios/" + ativo);
		Scanner sc = new Scanner(csv);
		boolean imprimir = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		List<FiiDividendo> retorno = new ArrayList<>();
		List<String> campos = new ArrayList<>();
		System.out.println(ativo);
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
								.valor(Double.parseDouble(campos.get(3).replace(",", "."))).build());
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
			System.out.println("Output from Server .... \n");
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

}
