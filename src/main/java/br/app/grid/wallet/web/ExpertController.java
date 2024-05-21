package br.app.grid.wallet.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.robo.RoboService;
import br.app.grid.wallet.usuario.UsuarioUtil;

@RestController
@RequestMapping("/expert")
public class ExpertController {

	@Autowired
	private AssinaturaService assinaturaService;
	@Autowired
	private RoboService expertService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/delta/{idAssinatura}")
	public ModelAndView delta(@PathVariable(name = "idAssinatura") Integer idAssinatura) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");
		Assinatura assinatura = assinaturaService.get(idAssinatura);
		List<AssinaturaExpert> experts = assinaturaService.getExperts(assinatura);
		for (AssinaturaExpert expert : experts) {
			assinaturaService.excluir(expert);
		}
		AssinaturaExpert expert = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
				.expert(expertService.getById("DELTA")).volume(1.0).volumeMaximo(1.0).build();
		assinaturaService.gravar(expert);
		return new ModelAndView("redirect:/detalhes/" + assinatura.getConta().getId());
	}
	

}
