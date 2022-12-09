package br.app.grid.wallet.usuario;

import javax.servlet.http.HttpServletRequest;

public class UsuarioUtil {

	public static boolean isLogged(HttpServletRequest request) {
		try {
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
			if (usuario == null)
				return false;
			return usuario.isAtivo();
		} catch (Exception e) {
			return false;
		}
	}

}
