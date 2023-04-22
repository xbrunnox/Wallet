package br.app.grid.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * <b>BusinessException</b><br>
 * Exceção de negócio.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 19 de abril de 2023.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class PreConditionException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor.
	 * 
	 * @param message Mensagem de erro.
	 */
	public PreConditionException(String message) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, message);
	}
}
