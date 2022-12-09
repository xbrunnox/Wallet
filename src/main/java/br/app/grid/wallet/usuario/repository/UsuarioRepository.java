package br.app.grid.wallet.usuario.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.app.grid.wallet.usuario.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

	@Query("FROM Usuario us WHERE us.email = :email AND us.senha = :senha AND us.ativo = true")
	Usuario autenticar(String email, String senha);

}
