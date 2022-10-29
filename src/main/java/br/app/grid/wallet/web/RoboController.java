package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.socket.Router;
import br.app.grid.wallet.socket.RouterStatusResponse;

@RestController
@RequestMapping("/robo")
public class RoboController {
	
	@Autowired
	private RoboService service;
	
	@GetMapping("/{id}")
	public Robo get(@PathVariable(name = "id") String id) {
		return service.getById(id);
	}
	
	@GetMapping("/status")
	public RouterStatusResponse status() {
		return Router.getInstance().getStatus();
	}

}
