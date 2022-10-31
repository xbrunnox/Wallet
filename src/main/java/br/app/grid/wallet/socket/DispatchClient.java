package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaResponse;
import lombok.Getter;

public class DispatchClient {

	private Socket socket;

	@Getter
	private String licenca;

	@Getter
	private String expiracao;

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

	private BufferedReader reader;

	private BufferedWriter writer;

	private DispatcherServer server;

	public DispatchClient(DispatcherServer server, Socket socket) {
		this.socket = socket;
		this.server = server;
		this.dataDeConexao = LocalDateTime.now();
		this.ip = socket.getInetAddress().getHostAddress();
		ultimaComunicacao = LocalDateTime.now();
		versao = "";
		tempoEnvio = "";
		autenticado = false;
		try {
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		monitorarConexao();
	}

	private void monitorarConexao() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Util.sleep(30000);
				while (ChronoUnit.SECONDS.between(ultimaComunicacao, LocalDateTime.now()) < 120) {
					Util.sleep(30000);
				}
				try {
					if (!socket.isClosed()) {
						System.out.println(
								DataConverter.nowBr() + " [USER] Desconectando por inatividade. [" + licenca + "]");
						socket.close();
					}
				} catch (IOException e) {
				}
			}
		}).start();
	}

	public void gerenciar() {
		if (reader == null || writer == null)
			return;
		System.out.println(DataConverter.nowBr() + " [USER] Aguardando conexão");
		String linha;
		try {
			linha = reader.readLine();
		} catch (IOException e) {
			return;
		}
		while (linha != null) {
			try {
				if (!linha.contains("ping"))
					System.out.println(DataConverter.nowBr() + " [USER] Recebido: " + linha);
				ultimaComunicacao = LocalDateTime.now();
				if (autenticado) {
					if (linha.equalsIgnoreCase("ping")) {
						sendPong();
					} else if (linha.startsWith("authenticate")) {
						autenticar(linha);
					}
				} else {
					if (linha.startsWith("authenticate")) {
						autenticar(linha);
					} else {
						System.out.println(DataConverter.nowBr() + " [USER] Desconectando intruso: " + ip);
						socket.close();
					}
				}
				linha = reader.readLine();
			} catch (Exception e) {
				System.out.println(DataConverter.nowBr() + " [USER] " + licenca + " " + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
	}

	private void autenticar(String linha) throws IOException {
		String[] parts = linha.split(" ");
		if (parts.length >= 2) {
			authenticate(parts[1]);
			if (parts.length >= 3)
				expert = parts[2];
			if (parts.length >= 4)
				versao = parts[3];
		} else {
			sendDisconnect("License Key nao informada.");
		}
	}

	private void authenticate(String licenseKey) throws IOException {
		ContaResponse licencaResponse = server.getLicencaService().autenticar(licenseKey);
		licenca = licencaResponse.getId();
		expiracao = licencaResponse.getExpiracao();

		Conta licenca = server.getLicencaService().get(licenseKey);

		if (licencaResponse.getAtivo()) {
			nome = licenca.getNome();
			send("authenticated " + licencaResponse.getExpiracao() + "#");
			autenticado = true;
		} else {
			autenticado = false;
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
		long inicio = System.currentTimeMillis();
		if (!command.contains("pong"))
			System.out.println(DataConverter.nowBr() + " [USER] Enviando: " + command);
		writer.write(command);
		writer.flush();
		tempoEnvio = (System.currentTimeMillis() - inicio) + " ms";
	}

	public void sendBuy(String ativo, Double volume) throws IOException {
		send("buy " + ativo + " " + volume + "#");
	}

	public void sendSell(String ativo, Double volume) throws IOException {
		send("sell " + ativo + " " + volume + "#");
	}
}
