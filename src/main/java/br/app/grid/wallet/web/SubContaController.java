package br.app.grid.wallet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.subconta.SubContaService;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 18 de abril de 2023.
 */
@RestController
@RequestMapping("/subconta")
public class SubContaController {

	@Autowired
	private SubContaService subcontaService;

	/**
	 * Realiza a associação da subconta à assinatura principal.
	 * 
	 * @param idAssinaturaPrincipal ID da assinatura principal.
	 * @param idSubConta            ID da subconta.
	 * @param idAutomacao           ID da automação.
	 */
	@GetMapping("/associar/{idAssinaturaPrincipal}/{idSubConta}/{idAutomacao}")
	public void associar(@PathVariable(name = "idAssinaturaPrincipal") Integer idAssinaturaPrincipal,
			@PathVariable(name = "idSubConta") String idSubConta,
			@PathVariable(name = "idAutomacao") String idAutomacao) {
		subcontaService.associar(idAssinaturaPrincipal, idSubConta, idAutomacao);
	}

}
