package br.app.grid.wallet.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.usuario.Usuario;
import br.app.grid.wallet.usuario.service.UsuarioService;
import br.app.grid.wallet.util.Md5Util;
import br.app.grid.wallet.web.request.CreateUserRequest;
import br.app.grid.wallet.web.request.UserLoginRequest;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private HttpServletRequest request;

	@PostMapping("/registrar")
	public ModelAndView registrar(CreateUserRequest createUser) {
		usuarioService.registrar(Usuario.builder().email(createUser.getEmail().trim()).nome(createUser.getNome().trim())
				.senha(Md5Util.toMd5(createUser.getSenha())).build());
		return new ModelAndView("redirect:/login");
	}

	@PostMapping("/login")
	public ModelAndView login(UserLoginRequest userLogin) {
		Usuario usuario = usuarioService.autenticar(userLogin.getEmail(), Md5Util.toMd5(userLogin.getSenha()));
		if (usuario != null) {
			request.getSession().setAttribute("usuario", usuario);
		} else {
			request.getSession().setAttribute("usuario", null);
			return new ModelAndView("redirect:/login");
		}
		System.out.println("Login: " + usuario.getNome());
		return new ModelAndView("redirect:/");
	}
	
	@PostMapping("/logout")
	public ModelAndView logout(CreateUserRequest createUser) {
		request.getSession().setAttribute("usuario", null);
		return new ModelAndView("redirect:/login");
	}

}
