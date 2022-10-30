package br.app.grid.wallet.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ExpertServer {

	private final int porta = 22341;

	private ServerSocket server;

	private static ExpertServer instancia;

	private List<ExpertClient> onlineClients;

	private Router router;

	private ExpertServer() {
		onlineClients = new ArrayList<>();
		try {
			server = new ServerSocket(porta);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ExpertServer getInstance() {
		if (instancia == null)
			instancia = new ExpertServer();
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
		ExpertClient client = new ExpertClient(this, socket);
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

	public List<ExpertUser> getOnline() {
		List<ExpertUser> retorno = new ArrayList<>();
		System.out.println("Sinais online: " + onlineClients.size());
		for (ExpertClient client : onlineClients) {
			retorno.add(ExpertUser.builder().dataDeConexao(DataConverter.formatarDateTimeBR(client.getDataDeConexao()))
					.ip(client.getIp()).nome(client.getNome())
					.ultimaComunicacao(DataConverter.formatarDateTimeBR(client.getUltimaComunicacao()))
					.versao(client.getVersao()).licenca(client.getLicenca()).tempoEnvio(client.getTempoEnvio())
					.expert(client.getExpert()).build());
		}
		return retorno;
	}

}