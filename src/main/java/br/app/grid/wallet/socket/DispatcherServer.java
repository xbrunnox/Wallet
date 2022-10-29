package br.app.grid.wallet.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.app.grid.wallet.licenca.LicensaService;

public class DispatcherServer {

	private final int porta = 22631;

	private ServerSocket server;
	private List<DispatchClient> onlineClients;
	private LicensaService licencaService;

	private static DispatcherServer instancia;

	private Router router;

	private DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private long tempoEnvio;

	private DispatcherServer() {
		onlineClients = new ArrayList<>();
		try {
			server = new ServerSocket(porta);
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
					System.out.println("Aguardando conexão na porta: " + porta);
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

//	public void comprar(String ativo, String volume) {
//		long inicio = System.currentTimeMillis();
//		for (DispatchClient client : onlineClients) {
//			try {
//				client.sendBuy(ativo, Integer.parseInt(volume));
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		tempoEnvio = System.currentTimeMillis() - inicio;
//
//	}

	public LicensaService getLicencaService() {
		return licencaService;
	}

	public void setLicencaService(LicensaService licencaService) {
		this.licencaService = licencaService;
	}

	public Router getRouter() {
		return router;
	}

	public List<SocketUser> getOnlineUsers() {
		List<SocketUser> retorno = new ArrayList<>();
		for (DispatchClient client : onlineClients) {
			retorno.add(SocketUser.builder().expiracao(client.getExpiracao())
					.dataDeConexao(formatador.format(client.getDataDeConexao())).ip(client.getIp())
					.nome(client.getNome()).ultimaComunicacao(formatData(client.getUltimaComunicacao()))
					.versao(client.getVersao()).licenca(client.getLicenca())
					.tempoEnvio(client.getTempoEnvio())
					.build());
		}
		return retorno;
	}

	private String formatData(LocalDateTime localDateTime) {
		if (localDateTime == null)
			return null;
		return formatador.format(localDateTime);
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public void sendBuy(String ativo, Double volume) {
		long inicio = System.currentTimeMillis();
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
		tempoEnvio = System.currentTimeMillis() - inicio;
	}

	public void sendSell(String ativo, Double volume) {
		long inicio = System.currentTimeMillis();
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
		tempoEnvio = System.currentTimeMillis() - inicio;
	}

	public long getTempoEnvio() {
		return tempoEnvio;
	}

	public void setTempoEnvio(long tempoEnvio) {
		this.tempoEnvio = tempoEnvio;
	}

}
