package br.app.grid.wallet.web;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.usuario.UsuarioUtil;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private AssinaturaService assinaturaService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/recebido")
	public ModelAndView recebido() {
		if (!UsuarioUtil.isLogged(request)) {
			return new ModelAndView("redirect:/login");
		}
		List<Pagamento> pagamentos = pagamentoService.getList();
		Collections.reverse(pagamentos);
		ModelAndView view = new ModelAndView("pagamento/recebido");
		view.addObject("pagamentosList", pagamentos);
		return view;
	}
	
	@GetMapping("/nao_associados")
	public ModelAndView naoAssociados() {
		if (!UsuarioUtil.isLogged(request)) {
			return new ModelAndView("redirect:/login");
		}
		List<Pagamento> pagamentos = pagamentoService.getListNaoAssociados();
		Collections.sort(pagamentos, new Comparator<Pagamento>() {
			@Override
			public int compare(Pagamento o1, Pagamento o2) {
				return o1.getDataAtualizacao().compareTo(o2.getDataAtualizacao());
			}
		});
		Collections.reverse(pagamentos);
		ModelAndView view = new ModelAndView("pagamento/nao_associado");
		view.addObject("pagamentosList", pagamentos);
		return view;
	}
	
	@GetMapping("/identificar/{idPagamento}")
	public void identificar(@PathVariable(name = "idPagamento") Long idPagamento) {
		assinaturaService.identificarPagamento(idPagamento);
	}
}
