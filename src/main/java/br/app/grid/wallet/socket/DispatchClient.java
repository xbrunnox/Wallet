package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import br.app.grid.wallet.licenca.Licenca;
import br.app.grid.wallet.licenca.LicencaResponse;
import br.app.grid.wallet.util.DataConverter;

public class DispatchClient {

	private String licenca;

	private String expiracao;

	private Socket socket;

	private LocalDateTime dataDeConexao;

	private LocalDateTime ultimaComunicacao;

	private String ip;

	private String nome;

	private String versao;

	private String tempoEnvio;

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
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
				}
				while (ChronoUnit.SECONDS.between(ultimaComunicacao, LocalDateTime.now()) < 120) {
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					if (!socket.isClosed()) {
						System.out.println(
								DataConverter.nowBr() + " [USER] " + licenca + " - Desconectando por inatividade.");
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
		System.out.println("Aguardando");
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
						String[] parts = linha.split(" ");
						if (parts.length >= 2) {
							authenticate(parts[1]);
							if (parts.length >= 3)
								versao = parts[2];
						} else {
							sendDisconnect("License Key nao informada.");
						}
					}
				} else {
					if (linha.startsWith("authenticate")) {
						String[] parts = linha.split(" ");
						if (parts.length >= 2) {
							authenticate(parts[1]);
							if (parts.length >= 3)
								versao = parts[2];
						} else {
							sendDisconnect("License Key nao informada.");
						}
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

	private void authenticate(String licenseKey) throws IOException {
		LicencaResponse licencaResponse = server.getLicencaService().autenticar(licenseKey);
		licenca = licencaResponse.getId();
		expiracao = licencaResponse.getExpiracao();

		Licenca licenca = server.getLicencaService().get(licenseKey);

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

	public String getLicenca() {
		return licenca;
	}

	public String getExpiracao() {
		return expiracao;
	}

	public LocalDateTime getDataDeConexao() {
		return dataDeConexao;
	}

	public String getIp() {
		return ip;
	}

	public void sendBuy(String ativo, Double volume) throws IOException {
		send("buy " + ativo + " " + volume + "#");
	}

	public void sendSell(String ativo, Double volume) throws IOException {
		send("sell " + ativo + " " + volume + "#");
	}

	public String getNome() {
		return nome;
	}

	public LocalDateTime getUltimaComunicacao() {
		return ultimaComunicacao;
	}

	public String getVersao() {
		return versao;
	}

	public String getTempoEnvio() {
		return tempoEnvio;
	}

}
