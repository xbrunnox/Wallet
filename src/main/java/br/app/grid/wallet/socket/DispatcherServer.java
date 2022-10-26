package br.app.grid.wallet.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import br.app.grid.wallet.licenca.LicensaService;

public class DispatcherServer {

	private ServerSocket server;
	private List<DispatchClient> onlineClients;
	private LicensaService licencaService;

	private static DispatcherServer instancia;

	private Router router;

	private DispatcherServer() {
		onlineClients = new ArrayList<>();
		try {
			server = new ServerSocket(7001);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DispatcherServer getInstance() {
		if (instancia == null)
			instancia = new DispatcherServer();
		return instancia;
	}

	public void aguardarConexao() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Aguardando conexão");
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

	public static void main(String[] args) {
		DispatcherServer server = new DispatcherServer();
		server.aguardarConexao();
	}

	protected void gerenciar(Socket socket) {
		DispatchClient client = new DispatchClient(this, socket);
		onlineClients.add(client);
		client.gerenciar();
		onlineClients.remove(client);
	}

	public void comprar(String ativo, String volume) {
		for (DispatchClient client : onlineClients) {
			try {
				client.sendBuy(ativo, Integer.parseInt(volume));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void vender(String ativo, String volume) {
		// TODO Auto-generated method stub

	}

	public LicensaService getLicencaService() {
		return licencaService;
	}

	public void setLicencaService(LicensaService licencaService) {
		this.licencaService = licencaService;
	}

	public List<SocketUser> getOnline() {
		List<SocketUser> retorno = new ArrayList<>();
		for (DispatchClient client : onlineClients) {
			retorno.add(SocketUser.builder().expiracao(client.getExpiracao()).licenca(client.getLicenca()).build());
		}
		return retorno;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public void sendBuy(String ativo, Double volume) {
		for (DispatchClient client : onlineClients) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						client.sendBuy(ativo, volume);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public void sendSell(String ativo, Double volume) {
		for (DispatchClient client : onlineClients) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						client.sendSell(ativo, volume);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

}
