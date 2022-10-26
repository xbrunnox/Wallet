package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import br.app.grid.wallet.licenca.LicencaResponse;

public class DispatchClient {

	private String licenca;

	private String expiracao;

	private Socket socket;

	private BufferedReader reader;

	private BufferedWriter writer;

	private DispatcherServer server;

	public DispatchClient(DispatcherServer server, Socket socket) {
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
		System.out.println("Aguardando");
		String linha;
		try {
			linha = reader.readLine();
		} catch (IOException e) {
			return;
		}
		while (linha != null) {
			try {
				System.out.println("Recebido: " + linha);
				if (linha.equalsIgnoreCase("ping")) {
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

	private void authenticate(String licenseKey) throws IOException {
		LicencaResponse licencaResponse = server.getLicencaService().autenticar(licenseKey);
		licenca = licencaResponse.getId();
		expiracao = licencaResponse.getExpiracao();

		if (licencaResponse.getAtivo()) {
			send("authenticated " + licencaResponse.getExpiracao() + "#");
		} else {
			sendDisconnect("License Key invalida ou expirada.");
		}
	}

	private void sendDisconnect(String string) throws IOException {
		send("disconnect " + string + "#");
	}

	private void sendPong() throws IOException {
		send("pong#");
	}

	public void sendBuy(String ativo, int volume) throws IOException {
		send("buy " + ativo + " " + volume + "#");
	}

	private synchronized void send(String command) throws IOException {
		System.out.println("Enviando: " + command);
		writer.write(command);
		writer.flush();
	}

	public String getLicenca() {
		return licenca;
	}

	public String getExpiracao() {
		return expiracao;
	}

	public void sendBuy(String ativo, Double volume) throws IOException {
		send("buy " + ativo + " " + volume + "#");
	}

	public void sendSell(String ativo, Double volume) throws IOException {
		send("sell " + ativo + " " + volume + "#");
	}

}
