package br.app.grid.wallet.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(7001);
			abrirConexao(server);

//			Socket socket = server.accept();
//			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//			System.out.println("Aguardando");
//			String linha = br.readLine();
//			while (linha != null) {
//				System.out.println("Recebido: "+linha);
//				bw.write("Pong");
//				linha = br.readLine();
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void abrirConexao(ServerSocket server) {
		System.out.println("Aguardando conexao");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Socket socket = server.accept();
					abrirConexao(server);
					gerenciar(socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();
	}

	protected static void gerenciar(Socket socket) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		System.out.println("Aguardando");
		String linha = br.readLine();
		while (linha != null) {
			System.out.println("Recebido: " + linha);
			if (linha.equalsIgnoreCase("ping")) {
				long inicio = System.currentTimeMillis();
				sendPong(bw);
				sendBuy(bw, "WINZ22", 2);
				System.out.println((System.currentTimeMillis() - inicio) + " ms");
			} else if (linha.startsWith("authenticate")) {
				String[] parts = linha.split(" ");
				if (parts.length >= 2) {
					authenticate(bw, parts[1]);
				} else {
					sendDisconnect(bw, "License Key nao informada.");
				}
			}
			linha = br.readLine();
		}

	}

	private static void authenticate(BufferedWriter bw, String licenseKey) throws IOException {
		// TODO fazer validacao em banco de dados
		if (licenseKey.equals("1234567890")) {
			bw.write("authenticated 31/12/2022#");
			bw.flush();
		} else {
			sendDisconnect(bw, "License Key invalida ou expirada.");
		}
	}

	private static void sendDisconnect(BufferedWriter bw, String string) throws IOException {
		bw.write("disconnect "+string+"#");
		bw.flush();
	}

	private static synchronized void sendPong(BufferedWriter bw) throws IOException {
		bw.write("pong#");
		bw.flush();
	}
	
	private static synchronized void sendBuy(BufferedWriter bw, String ativo, int volume) throws IOException {
		bw.write("buy "+ativo+" "+volume+"#");
		bw.flush();
	}
}
