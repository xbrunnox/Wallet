package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import lombok.Getter;

public class ExpertClient {
	private Socket socket;

	private BufferedReader reader;

	private BufferedWriter writer;

	private ExpertServer server;

	@Getter
	private String licenca;

	@Getter
	private LocalDateTime dataDeConexao;

	@Getter
	private LocalDateTime ultimaComunicacao;

	@Getter
	private String ip;

	@Getter
	private String expert;

	@Getter
	private String nome;

	@Getter
	private String versao;

	@Getter
	private String tempoEnvio;

	@Getter
	private boolean autenticado;

	public ExpertClient(ExpertServer server, Socket socket) {
		this.socket = socket;
		this.server = server;
		this.dataDeConexao = LocalDateTime.now();
		this.ip = socket.getInetAddress().getHostAddress();
		ultimaComunicacao = LocalDateTime.now();
		versao = "";
		tempoEnvio = "";
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
		System.out.println("Aguardando msg EXPERT]");
		String linha;
		try {
			linha = reader.readLine();
		} catch (IOException e) {
			return;
		}
		while (linha != null) {
			ultimaComunicacao = LocalDateTime.now();
			try {
				if (!linha.contains("ping"))
					System.out.println(DataConverter.nowBr() + " Recebido [EXPERT]: " + linha);
				if (linha.startsWith("buy")) {
					String[] parts = linha.split(" ");
					if (parts.length >= 4) {
						String origem = parts[1];
						String ativo = parts[2];
						Double volume = Double.valueOf(parts[3]);
						server.sendBuy(ativo, volume);
					}
				} else if (linha.startsWith("sell")) {
					String[] parts = linha.split(" ");
					if (parts.length >= 4) {
						String origem = parts[1];
						String ativo = parts[2];
						Double volume = Double.valueOf(parts[3]);
						server.sendSell(ativo, volume);
					}
				} else if (linha.equalsIgnoreCase("ping")) {
					sendPong();
				} else if (linha.startsWith("authenticate")) {
					autenticar(linha);
				}
				linha = reader.readLine();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private void autenticar(String comando) throws IOException {
		String[] parts = comando.split(" ");
		if (parts.length >= 2) {
			licenca = parts[1];
			authenticate(parts[1]);
			if (parts.length >= 3)
				expert = parts[2];
			if (parts.length >= 4)
				versao = parts[3];
		} else {
			sendDisconnect("License Key nao informada.");
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
		long inicio = System.currentTimeMillis();
		if (!command.contains("pong"))
			System.out.println(DataConverter.nowBr() + " [EXPERT] Enviando: " + command);
		writer.write(command);
		writer.flush();
		tempoEnvio = (System.currentTimeMillis() - inicio) + " ms";
	}

}
