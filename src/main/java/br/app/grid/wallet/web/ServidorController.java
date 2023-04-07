package br.app.grid.wallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.grid.wallet.enums.ServidorTipoEnum;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.servidor.ServidorService;

/**
 * ServidorController
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 26 de fevereiro de 2023.
 */
@RestController
@RequestMapping("/servidor")
public class ServidorController {

	@Autowired
	private ServidorService servidorService;

	@GetMapping("/listar/{tipo}/{ativo}")
	public List<Servidor> listar(@PathVariable(name = "tipo") ServidorTipoEnum tipo,
			@PathVariable(name = "ativo") Boolean ativo) {
		return servidorService.getList(tipo, ativo);
	}

}