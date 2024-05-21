package br.app.grid.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * <b>UnauthorizedException</b><br>
 * Exceção de permissão.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 15 de dezembro de 2023.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor.
	 * 
	 * @param message Mensagem de erro.
	 */
	public UnauthorizedException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
