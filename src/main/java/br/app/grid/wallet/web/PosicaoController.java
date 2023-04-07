package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.router.RouterService;

@RestController
@RequestMapping("/posicao")
public class PosicaoController {
	
	@Autowired
	private RouterService routerService;

	@GetMapping("/fechar/{conta}/{ativo}")
	public void fechar(@PathVariable(name = "conta") String conta, @PathVariable(name = "ativo") String ativo) {
		routerService.fecharPosicao(conta, ativo);
	}

}
