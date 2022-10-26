package br.app.grid.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.app.grid.wallet.licenca.LicensaService;
import br.app.grid.wallet.socket.Router;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired
	private LicensaService licencaService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Router router = Router.getInstance();
		router.setLicencaService(licencaService);
		System.out.println("Aplicacao subiu");
	}

}
