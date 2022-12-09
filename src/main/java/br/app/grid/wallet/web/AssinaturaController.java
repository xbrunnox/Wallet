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
import br.app.grid.wallet.assinatura.AssinaturaExpertsResponse;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaService;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.usuario.UsuarioUtil;

@RestController
@RequestMapping("/assinatura")
public class AssinaturaController {

	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private ContaService contaService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/experts/{conta}")
	public AssinaturaExpertsResponse experts(@PathVariable("conta") String conta) {
		return assinaturaService.getExpertsAtivos(conta);
	}

	@GetMapping("/iniciarAtivacao/{conta}")
	public ModelAndView iniciarAtivacao(@PathVariable("conta") String idConta) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");

		Conta conta = contaService.get(idConta);
		List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();

		ModelAndView view = new ModelAndView("assinatura/iniciar_ativacao");
		view.addObject("conta", conta);
		view.addObject("pagamentosList", pagamentos);
		return view;
	}

	@GetMapping("/associarPagamento/{idAssinatura}")
	public ModelAndView associarPagamento(@PathVariable("idAssinatura") Integer idAssinatura) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");

		Assinatura assinatura = assinaturaService.get(idAssinatura);

		List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();

		ModelAndView view = new ModelAndView("assinatura/associar_pagamento");
		view.addObject("assinatura", assinatura);
		view.addObject("conta", assinatura.getConta());
		view.addObject("pagamentosList", pagamentos);
		return view;
	}

	@GetMapping("/associarPagamento/{idAssinatura}/{idPagamento}")
	public ModelAndView associarPagamento(@PathVariable("idAssinatura") Integer idAssinatura,
			@PathVariable("idPagamento") Long idPagamento) {
		if (!UsuarioUtil.isLogged(request))
			return new ModelAndView("redirect:/login");

		assinaturaService.associarPagamento(idAssinatura, idPagamento);

		ModelAndView view = new ModelAndView("redirect:/ativas");
		return view;
	}

	@GetMapping("/ativar/{conta}/{pagamento}")
	public ModelAndView ativar(@PathVariable("conta") String idConta, @PathVariable("pagamento") Long idPagamento) {
		System.out.println("Ativando Conta: " + idConta + " com o pagamento: " + idPagamento);
		if (UsuarioUtil.isLogged(request)) {
			Assinatura assinatura = assinaturaService.ativar(idConta, idPagamento);
			return new ModelAndView("redirect:/inativas");
		} else {
			return new ModelAndView("redirect:/login");
		}
	}

	@GetMapping("/ativarComPendencia/{conta}")
	public ModelAndView ativarComPendencia(@PathVariable("conta") String idConta) {
		if (UsuarioUtil.isLogged(request)) {
			Assinatura assinatura = assinaturaService.ativarComPendencia(idConta);
			return new ModelAndView("redirect:/inativas");
		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	@GetMapping("/todas")
	public ModelAndView todas() {
		if (UsuarioUtil.isLogged(request)) {
			List<Assinatura> assinaturasAtivas = assinaturaService.getList();
			ModelAndView view = new ModelAndView("assinatura/todas");
			view.addObject("assinaturasList", assinaturasAtivas);
			return view;
		} else {
			return new ModelAndView("redirect:/login");
		}
	}

}
