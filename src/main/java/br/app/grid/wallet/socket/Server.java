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
			System.out.println("Aguardando conexao");
			Socket socket = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Aguardando");
			String linha = br.readLine();
			while (linha != null) {
				System.out.println("Recebido: "+linha);
				bw.write("Pong");
				linha = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
