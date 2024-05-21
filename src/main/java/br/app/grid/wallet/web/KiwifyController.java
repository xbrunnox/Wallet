package br.app.grid.wallet.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.afiliado.AfiliadoLojaService;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.kiwify.KiwifyEvent;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;
import br.app.grid.wallet.util.VersatilUtil;

/**
 * <b>KiwifyController</b><br>
 * 
 * Controlador responsável em tratar as chamadas WEB do Kiwify.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 5 de dezembro de 2022.
 */
@RestController
@RequestMapping("/kiwify")
public class KiwifyController {

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private AssinaturaService assinaturaService;

  @Autowired
  private AfiliadoLojaService afiliadoLojaService;

  @PostMapping("/pagamento")
  public String pagamento(@RequestBody KiwifyEvent request) {
    System.out.println("Recebendo WebHook de Pagamento: " + VersatilUtil.toJson(request));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    Afiliado afiliado = afiliadoLojaService.getAfiliadoByIdLoja(request.getStoreId());
    Pagamento pagamento = Pagamento.builder().afiliado(afiliado).cpf(request.getCustomer().getCpf())
        .dataAtualizacao(LocalDateTime.parse(request.getUpdatedAt(), formatter))
        .dataCriacao(LocalDateTime.parse(request.getCreatedAt(), formatter))
        .email(request.getCustomer().getEmail()).formaDePagamento(request.getPaymentMethod())
        .idCliente(request.getPaymentMerchantId()).idLoja(request.getStoreId())
        .nome(request.getCustomer().getFull_name()).produto(request.getProduct().getProduct_name())
        .idPedido(request.getOrderId()).idProduto(request.getProduct().getProduct_id())
        .ip(request.getCustomer().getIp()).refPedido(request.getOrderRef())
        .status(request.getOrderStatus()).taxas(request.getCommissions().getKiwify_fee() / 100)
        .telefone(request.getCustomer().getMobile())
        .valor(request.getCommissions().getProduct_base_price() / 100).plataforma("Kiwify").build();
    pagamentoService.gravar(pagamento);
    assinaturaService.identificarPagamento(pagamento.getId());
    return "ok";
  }

}
