package br.app.grid.wallet.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.licenca.ContaInfoResponse;
import br.app.grid.wallet.licenca.ContaResponse;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.socket.ClienteUser;
import br.app.grid.wallet.socket.DispatcherServer;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.web.request.AutenticarRequest;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaService service;
	
	@Autowired
	private HttpServletRequest request;

	@PostMapping("/autenticar")
	public ContaResponse autenticar(@RequestBody AutenticarRequest request) {
		return service.autenticar(request.getNome(), request.getCorretora(), request.getConta());
	}

	@GetMapping("/check/{license}")
	public ContaResponse check(@PathVariable(name = "license", required = true) String license) {
		return service.autenticar(license);
	}
	
	@GetMapping("/online")
	public List<ClienteUser> online() {
		return DispatcherServer.getInstance().getOnlineUsers();
	}
	
	@GetMapping("/info/{license}")
	public ContaInfoResponse info(@PathVariable(name = "license", required = true) String license) {
		return service.info(license);
	}
	
	@GetMapping("/excluir/{conta}")
	public void excluir(@PathVariable(name = "conta", required = true) String idConta) {
		if (!UsuarioUtil.isLogged(request))
			throw new RuntimeException("Usuario não está logado.");
		service.excluir(idConta);
	}
}