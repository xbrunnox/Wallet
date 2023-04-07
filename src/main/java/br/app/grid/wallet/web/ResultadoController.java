package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.response.ResultadoGlobalResponse;
import br.app.grid.wallet.resultado.ResultadoService;

/**
 * <b>ResultadoController</b>
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de abril de 2023.
 */
@RestController
@RequestMapping("/resultado")
public class ResultadoController {

	@Autowired
	private ResultadoService resultadoService;

	@GetMapping("/global/{expert}")
	public ResultadoGlobalResponse global(@PathVariable("expert") String expert) {
		return resultadoService.getResultadoGlobal(expert);
	}

}
