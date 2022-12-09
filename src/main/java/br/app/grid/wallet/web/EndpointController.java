package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.meta.PosicoesMT;

@RestController
@RequestMapping("/endpoint")
public class EndpointController {

	@Autowired
	private EndpointService endpointService;

	@RequestMapping("/conexoes")
	public void conexoes() {

	}

	@RequestMapping("/posicoes/{conta}")
	public ModelAndView posicoes(@PathVariable(name = "conta") String conta) {
		PosicoesMT posicoes = endpointService.getPosicoes(conta);

		ModelAndView modelAndView = new ModelAndView("endpoint/posicoes");
		modelAndView.addObject("posicoes", posicoes);
		return modelAndView;
	}
	
	@RequestMapping("/status")
	public EndpointStatusResponse status() {
		return endpointService.getStatus();
	}
	
}
