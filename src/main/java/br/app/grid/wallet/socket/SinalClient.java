package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SinalClient {

	private BufferedReader reader;

	private BufferedWriter writer;

	private SinalServer server;

	private Socket socket;

	public SinalClient(SinalServer server, Socket socket) {
		this.socket = socket;
		this.server = server;
		try {
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerenciar() {
		if (reader == null || writer == null)
			return;
		System.out.println("Aguardando msg (Sinal)");
		String linha;
		try {
			linha = reader.readLine();
		} catch (IOException e) {
			return;
		}
		while (linha != null) {
			try {
				System.out.println("Recebido: " + linha);
				if (linha.startsWith("buy")) {
					String[] parts = linha.split(" ");
					if (parts.length >= 3) {
						String ativo = parts[1];
						Double volume = Double.valueOf(parts[2]);
						server.sendBuy(ativo, volume);
					}
				} else if (linha.startsWith("sell")) {
					String[] parts = linha.split(" ");
					if (parts.length >= 3) {
						String ativo = parts[1];
						Double volume = Double.valueOf(parts[2]);
						server.sendSell(ativo, volume);
					}
				} else if (linha.equalsIgnoreCase("ping")) {
					long inicio = System.currentTimeMillis();
					sendPong();
					System.out.println((System.currentTimeMillis() - inicio) + " ms");
				} else if (linha.startsWith("authenticate")) {
					String[] parts = linha.split(" ");
					if (parts.length >= 2) {
						authenticate(parts[1]);
					} else {
						sendDisconnect("License Key nao informada.");
					}
				}
				linha = reader.readLine();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private void sendPong() throws IOException {
		send("pong#");
	}

	private void sendDisconnect(String string) throws IOException {
		send("disconnect " + string + "#");
	}

	private void authenticate(String licenseKey) throws IOException {
//		LicencaResponse licencaResponse = server.getLicencaService().autenticar(licenseKey);
//
//		if (licencaResponse.getAtivo()) {
//			send("authenticated " + licencaResponse.getExpiracao() + "#");
//		} else {
//			sendDisconnect("License Key invalida ou expirada.");
//		}
	}

	private synchronized void send(String command) throws IOException {
		System.out.println("Enviando: " + command);
		writer.write(command);
		writer.flush();
	}

}
