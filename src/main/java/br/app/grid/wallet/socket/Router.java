package br.app.grid.wallet.socket;

import java.util.List;

import br.app.grid.wallet.licenca.LicensaService;

public class Router {

	public static Router router;

	private SinalServer sinalServer;
	private DispatcherServer dispatcherServer;

	private LicensaService licencaService;

	private Router() {
		sinalServer = SinalServer.getInstance();
		dispatcherServer = DispatcherServer.getInstance();
		sinalServer.setRouter(this);
		dispatcherServer.setRouter(this);
		sinalServer.aguardarConexao();
		dispatcherServer.aguardarConexao();
	}

	public LicensaService getLicencaService() {
		return licencaService;
	}

	public static Router getInstance() {
		if (router == null) {
			router = new Router();
		}
		return router;
	}

	public void setLicencaService(LicensaService licencaService) {
		this.licencaService = licencaService;
		dispatcherServer.setLicencaService(this.licencaService);
	}

	public void sendBuy(String ativo, Double volume) {
		System.out.println("Router: buy " + ativo + " " + volume);
		dispatcherServer.sendBuy(ativo, volume);
	}

	public void sendSell(String ativo, Double volume) {
		System.out.println("Router: sell " + ativo + " " + volume);
		dispatcherServer.sendSell(ativo, volume);
	}

	public RouterStatusResponse getStatus() {
		List<SocketUser> onlineUser = dispatcherServer.getOnlineUsers();
		return RouterStatusResponse.builder().onlineUsers(onlineUser).tempoEnvio(dispatcherServer.getTempoEnvio() + " ms")
				.build();
	}
}
