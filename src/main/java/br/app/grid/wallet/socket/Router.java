package br.app.grid.wallet.socket;

import java.util.List;

import br.app.grid.wallet.licenca.ContaService;

public class Router {

	public static Router router;

	private ExpertServer sinalServer;
	private DispatcherServer dispatcherServer;

	private ContaService licencaService;

	private Router() {
		sinalServer = ExpertServer.getInstance();
		dispatcherServer = DispatcherServer.getInstance();
		sinalServer.setRouter(this);
		dispatcherServer.setRouter(this);
		sinalServer.aguardarConexao();
		dispatcherServer.aguardarConexao();
	}

	public ContaService getLicencaService() {
		return licencaService;
	}

	public static Router getInstance() {
		if (router == null) {
			router = new Router();
		}
		return router;
	}

	public void setLicencaService(ContaService licencaService) {
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
		List<ClienteUser> onlineUser = dispatcherServer.getOnlineUsers();
		List<ExpertUser> onlineExperts = sinalServer.getOnline();
		return RouterStatusResponse.builder().onlineUsers(onlineUser)
				.tempoEnvio(dispatcherServer.getTempoEnvio() + " ms").onlineExperts(onlineExperts).build();
	}
}
