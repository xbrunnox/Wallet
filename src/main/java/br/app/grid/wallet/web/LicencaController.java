package br.app.grid.wallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.licenca.LicencaResponse;
import br.app.grid.wallet.licenca.LicensaService;
import br.app.grid.wallet.socket.DispatcherServer;
import br.app.grid.wallet.socket.ClienteUser;
import br.app.grid.wallet.web.request.AutenticarRequest;

@RestController
@RequestMapping("/licenca")
public class LicencaController {

	@Autowired
	private LicensaService service;

	@PostMapping("/autenticar")
	public LicencaResponse autenticar(@RequestBody AutenticarRequest request) {
		return service.autenticar(request.getNome(), request.getCorretora(), request.getConta());
	}

	@GetMapping("/check/{license}")
	public LicencaResponse check(@PathVariable(name = "license", required = true) String license) {
		return service.autenticar(license);
	}
	
	@GetMapping("/online")
	public List<ClienteUser> online() {
		return DispatcherServer.getInstance().getOnlineUsers();
	}
}