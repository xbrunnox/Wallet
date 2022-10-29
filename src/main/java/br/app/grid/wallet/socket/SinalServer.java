package br.app.grid.wallet.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SinalServer {
	
	private final int porta = 22341;
	
	private ServerSocket server;
	
	private static SinalServer instancia;
	
	private List<SinalClient> onlineClients;
	
	private Router router;
	
	private SinalServer() {
		onlineClients = new ArrayList<>();
		try {
			server = new ServerSocket(porta);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SinalServer getInstance() {
		if (instancia == null)
			instancia = new SinalServer();
		return instancia;
	}
	
	public void aguardarConexao() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Aguardando conexão de sinais");
					Socket socket = server.accept();
					aguardarConexao();
					gerenciar(socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();

	}
	
	protected void gerenciar(Socket socket) {
		SinalClient client = new SinalClient(this, socket);
		onlineClients.add(client);
		client.gerenciar();
		onlineClients.remove(client);
	}

	public void sendBuy(String ativo, Double volume) {
		router.sendBuy(ativo, volume);
	}
	
	public void sendSell(String ativo, Double volume) {
		router.sendSell(ativo, volume);
	}

	public void setRouter(Router router) {
		this.router = router;
		
	}

}
