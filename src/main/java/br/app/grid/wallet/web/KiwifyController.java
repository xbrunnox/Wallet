package br.app.grid.wallet.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.kiwify.KiwifyEvent;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoService;

@RestController
@RequestMapping("/kiwify")
public class KiwifyController {

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private AssinaturaService assinaturaService;

  @PostMapping("/pagamento")
  public String pagamento(@RequestBody KiwifyEvent request) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    Pagamento pagamento = Pagamento.builder().cpf(request.getCustomer().getCpf())
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
