package br.app.grid.wallet.usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.grid.wallet.usuario.Usuario;
import br.app.grid.wallet.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void registrar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	public Usuario autenticar(String email, String senha) {
		return usuarioRepository.autenticar(email, senha);
	}

}
