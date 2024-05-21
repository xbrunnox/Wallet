package br.app.grid.wallet.assinatura.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.AssinaturaExpert;
import br.app.grid.wallet.assinatura.AssinaturaExpertsResponse;
import br.app.grid.wallet.assinatura.AssinaturaPagamento;
import br.app.grid.wallet.assinatura.repository.AssinaturaAtivaRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaExpertRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaPagamentoRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaPendenciaRepository;
import br.app.grid.wallet.assinatura.repository.AssinaturaRepository;
import br.app.grid.wallet.assinatura.view.AssinaturaAtivaView;
import br.app.grid.wallet.assinatura.view.AssinaturaPendenciaView;
import br.app.grid.wallet.assinatura.view.AssinaturaResultadoView;
import br.app.grid.wallet.assinatura.view.AssinaturaView;
import br.app.grid.wallet.exception.BusinessException;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaRepository;
import br.app.grid.wallet.pagamento.Pagamento;
import br.app.grid.wallet.pagamento.PagamentoRepository;
import br.app.grid.wallet.produto.Produto;
import br.app.grid.wallet.produto.ProdutoService;
import br.app.grid.wallet.produto.ProdutoTipo;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboRepository;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.servidor.ServidorAlocacao;
import br.app.grid.wallet.servidor.ServidorRepository;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.repository.TradeRepository;
import br.app.grid.wallet.web.request.AdicionarExpertAssinaturaRequest;
import br.app.grid.wallet.web.request.AlterarEmailRequest;
import br.app.grid.wallet.web.request.AlterarVencimentoRequest;
import br.app.grid.wallet.web.request.AssociarTratarPagamentoRequest;
import br.app.grid.wallet.web.request.AtivarAssinaturaRequest;
import br.app.grid.wallet.web.request.ExcluirAssinaturaPagamentoRequest;

@Service
public class AssinaturaService {

  @Autowired
  private AssinaturaRepository assinaturaRepository;

  @Autowired
  private AssinaturaPagamentoRepository assinaturaPagamentoRepository;

  @Autowired
  private AssinaturaAtivaRepository assinaturaAtivaRepository;

  @Autowired
  private AssinaturaPendenciaRepository assinaturaPendenciaRepository;

  @Autowired
  private AssinaturaExpertRepository assinaturaExpertRepository;

  @Autowired
  private RoboRepository roboRepository;

  @Autowired
  private PagamentoRepository pagamentoRepository;

  @Autowired
  private ProdutoService produtoService;

  @Autowired
  private ContaRepository contaRepository;

  @Autowired
  private TradeRepository tradeRepository;

  @Autowired
  private ServidorRepository servidorRepository;

  public AssinaturaExpertsResponse getExpertsAtivos(String conta) {
    return AssinaturaExpertsResponse.builder()
        .experts(assinaturaExpertRepository.getListAtivados(conta, LocalDate.now())).conta(conta)
        .build();
  }

  public List<Assinatura> getList() {
    return assinaturaRepository.getList();
  }

  public List<Assinatura> getListAtivas() {
    LocalDate data = LocalDate.now();
    return assinaturaRepository.getListAtivas(data);
  }

  public List<Conta> getContasInativas() {
    // List<AssinaturaAtivaView> assinaturasAtivas =
    // assinaturaAtivaRepository.getList(LocalDate.now());
    List<AssinaturaView> assinaturas = assinaturaRepository.getListView();
    // List<Assinatura> assinaturasAtivas = getListAtivas();
    Map<String, AssinaturaView> mapaAssinaturas = new HashMap<>();
    for (AssinaturaView assinatura : assinaturas) {
      if (assinatura.getDataVencimento().compareTo(LocalDate.now()) >= 0)
        mapaAssinaturas.put(assinatura.getConta(), assinatura);
    }
    List<Conta> contas = contaRepository.getList();
    for (int i = contas.size() - 1; i >= 0; i--) {
      Conta conta = contas.get(i);
      if (mapaAssinaturas.containsKey(conta.getId())) {
        contas.remove(i);
      }
    }
    return contas;
  }

  @Deprecated
  public Assinatura ativar(String idConta, Long idPagamento) {
    Conta conta = contaRepository.findById(idConta).get();
    Pagamento pagamento = pagamentoRepository.findById(idPagamento).get();

    Robo dolar = roboRepository.findById("WDO01").get();
    Robo indice = roboRepository.findById("WIN01").get();
    Robo delta = roboRepository.findById("DELTA").get();

    if (conta != null && pagamento != null) {
      Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
          .dataVencimento(LocalDate.now().plusMonths(1))
          .documentoPagamento(DocumentoFormatter.format(pagamento.getCpf()))
          .emailPagamento(pagamento.getEmail().toLowerCase()).telefone(pagamento.getTelefone())
          .servidor(getServidorParaAlocacao()).pausado(true).build();
      assinaturaRepository.save(assinatura);
      // Expert Dolar
      AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
          .expert(dolar).volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
      // assinaturaExpertRepository.save(expertDolar);
      // Expert Indice
      AssinaturaExpert expertIndice =
          AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(indice)
              .volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
      // assinaturaExpertRepository.save(expertIndice);
      // Expert Delta
      AssinaturaExpert expertDelta = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
          .expert(delta).volume(delta.getVolume()).volumeMaximo(delta.getVolume()).build();
      assinaturaExpertRepository.save(expertDelta);
      assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
          .dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());

      pagamento.setAssociado(true);
      pagamentoRepository.save(pagamento);
      return assinatura;
    }

    return null;
  }



  /**
   * Retorna o próximo servidor para alocação, ou seja, o servidor com menos usuários
   * 
   * @return Servidor.
   */
  public Servidor getServidorParaAlocacao() {
    List<ServidorAlocacao> alocacoes = servidorRepository.getListAlocacao();
    Servidor servidor = servidorRepository.getById(alocacoes.get(0).getId());
    return servidor;
  }

  public Assinatura ativarComPendencia(String idConta) {
    Conta conta = contaRepository.findById(idConta).get();

    Robo dolar = roboRepository.findById("WDO01").get();
    Robo indice = roboRepository.findById("WIN01").get();
    Robo delta = roboRepository.findById("DELTA").get();

    if (conta != null) {
      Assinatura assinatura = Assinatura.builder().conta(conta).dataCadastro(LocalDateTime.now())
          .dataVencimento(LocalDate.now().plusMonths(1)).servidor(getServidorParaAlocacao())
          .pausado(true).build();
      assinaturaRepository.save(assinatura);
      // Expert Dolar
      AssinaturaExpert expertDolar = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
          .expert(dolar).volume(dolar.getVolume()).volumeMaximo(dolar.getVolume()).build();
      // assinaturaExpertRepository.save(expertDolar);
      // Expert Indice
      AssinaturaExpert expertIndice =
          AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(indice)
              .volume(indice.getVolume()).volumeMaximo(indice.getVolume()).build();
      // assinaturaExpertRepository.save(expertIndice);
      // Expert Delta
      AssinaturaExpert expertDelta = AssinaturaExpert.builder().assinatura(assinatura).ativado(true)
          .expert(delta).volume(delta.getVolume()).volumeMaximo(delta.getVolume()).build();
      assinaturaExpertRepository.save(expertDelta);

      return assinatura;
    }

    return null;
  }

  public List<AssinaturaPendenciaView> getPendencias() {
    return assinaturaPendenciaRepository.getList();
  }

  public List<AssinaturaAtivaView> getAtivas() {
    return assinaturaAtivaRepository.getList(LocalDate.now());
  }

  /**
   * Retorna a lista de assinaturas ativas do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @param contabilizarResultado Contabilizar resultados.
   * @return Lista de assinaturas ativas.
   */
  public List<AssinaturaView> getAtivas(Afiliado afiliado, boolean contabilizarResultados) {
    if (Objects.isNull(afiliado)) {
      return new ArrayList<>();
    }
    List<AssinaturaView> assinaturas =
        assinaturaRepository.getListView(afiliado.getId(), LocalDate.now());
    if (contabilizarResultados) {
      Map<String, AssinaturaView> mapaAssinaturas = new HashMap<>();
      for (AssinaturaView assinatura : assinaturas)
        mapaAssinaturas.put(assinatura.getConta(), assinatura);
      List<AssinaturaResultadoView> assinaturaResultados =
          assinaturaRepository.getListResultadosView(afiliado.getId());
      for (AssinaturaResultadoView assinaturaResultado : assinaturaResultados) {
        AssinaturaView assinatura = mapaAssinaturas.get(assinaturaResultado.getConta());
        if (!Objects.isNull(assinatura))
          assinatura.setResultado(assinaturaResultado.getResultado());
      }
    }
    return assinaturas;
  }

  public void migrar(String origem, String destino) {
    List<Assinatura> assinaturas = assinaturaRepository.getList(origem);
    List<Assinatura> assinaturasDestino = assinaturaRepository.getList(destino);
    Assinatura assinaturaDestino =
        (assinaturasDestino.size() == 1 ? assinaturasDestino.get(0) : null);
    Conta contaDestino = contaRepository.findById(destino).get();
    if (assinaturaDestino == null) {
      for (Assinatura assinatura : assinaturas) {
        assinatura.setConta(contaDestino);
        assinatura.setPausado(false);
        assinaturaRepository.save(assinatura);
      }
      List<Trade> trades = tradeRepository.getList(origem);
      for (Trade trade : trades) {
        trade.setConta(contaDestino);
        tradeRepository.save(trade);
      }
    } else {
      List<Trade> trades = tradeRepository.getList(origem);
      for (Trade trade : trades) {
        trade.setConta(contaDestino);
        tradeRepository.save(trade);
      }
      List<AssinaturaPagamento> pagamentos =
          assinaturaPagamentoRepository.getList(assinaturas.get(0).getId());
      for (AssinaturaPagamento pagamento : pagamentos) {
        pagamento.setAssinatura(assinaturaDestino);
        assinaturaPagamentoRepository.save(pagamento);
      }
      assinaturaRepository.delete(assinaturas.get(0));
    }
  }

  public void gravar(Assinatura assinatura) {
    assinaturaRepository.save(assinatura);
  }

  public Assinatura get(Integer idAssinatura) {
    return assinaturaRepository.get(idAssinatura);
  }

  public Assinatura associarPagamento(Integer idAssinatura, Long idPagamento) {
    Assinatura assinatura = assinaturaRepository.get(idAssinatura);
    Pagamento pagamento = pagamentoRepository.findById(idPagamento).get();

    assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
        .dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());

    pagamento.setAssociado(true);
    pagamentoRepository.save(pagamento);

    return assinatura;
  }

  public void identificarPagamento(Long idPagamento) {
    if (idPagamento == null || idPagamento == 0)
      return;
    // TODO
    Pagamento pagamento = pagamentoRepository.get(idPagamento);
    if (pagamento != null) {
      if (pagamento.isAssociado()) {
        System.out.println("Pagamento ja associado");
        return;
      }
      Produto produto = produtoService.getByNome(pagamento.getProduto());
      if (produto != null) {
        if (produto.getTipo() == ProdutoTipo.ASSINATURA_MENSAL) {
          List<Assinatura> assinaturas =
              assinaturaRepository.getListHabilitadasByEmail(pagamento.getEmail().toLowerCase());
          Assinatura assinatura = null;
          for (Assinatura ass : assinaturas) {
            if ((assinatura == null
                || ass.getDataVencimento().compareTo(assinatura.getDataVencimento()) < 0)
                && ass.getAssinaturaPrincipal() == null) {
              assinatura = ass;
            }
          }
          if (assinatura != null) {
            AssinaturaPagamento assPagamento = AssinaturaPagamento.builder().assinatura(assinatura)
                .dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build();
            assinaturaPagamentoRepository.save(assPagamento);
            pagamento.setAssociado(true);
            pagamentoRepository.save(pagamento);
            if (assinatura.getDataVencimento()
                .compareTo(pagamento.getDataAtualizacao().toLocalDate()) > 0) {
              assinatura.setDataVencimento(assinatura.getDataVencimento().plusMonths(1));
            } else {
              assinatura
                  .setDataVencimento(pagamento.getDataAtualizacao().toLocalDate().plusMonths(1));
            }
            assinaturaRepository.save(assinatura);
            System.out.println("Pagamento identificado.[" + idPagamento
                + "] Assinatura encontrada. [" + assinatura.getConta().getId() + "]");

            // Atualizando subcontas
            List<Assinatura> assinaturaSubContas = getListSubContas(assinatura.getId());
            if (!Objects.isNull(assinaturaSubContas)) {
              for (Assinatura subAssinatura : assinaturaSubContas) {
                subAssinatura.setDataVencimento(assinatura.getDataVencimento());
                assinaturaRepository.save(subAssinatura);
              }
            }
          } else {
            System.out.println("Pagamento não identificado. Assinatura não encontrada.");
          }
        }
      } else {
        System.out.println("Produto não encontrado");
      }
    }

  }

  public List<Assinatura> getList(String idConta) {
    return assinaturaRepository.getList(idConta);
  }

  public List<AssinaturaPagamento> getListPagamentos(String conta) {
    return assinaturaPagamentoRepository.getListPagamentos(conta);
  }

  public List<AssinaturaPagamento> getListPagamentos(Assinatura assinatura) {
    return assinaturaPagamentoRepository.getListPagamentos(assinatura.getId());
  }

  public void gravar(AssinaturaPagamento pagamento) {
    assinaturaPagamentoRepository.save(pagamento);

  }

  public void excluir(Assinatura assinatura) {
    assinaturaRepository.delete(assinatura);
  }

  public List<AssinaturaExpert> getExperts(Assinatura assinatura) {
    return assinaturaExpertRepository.getList(assinatura);
  }

  public void excluir(AssinaturaExpert expert) {
    assinaturaExpertRepository.delete(expert);

  }

  public void gravar(AssinaturaExpert expert) {
    assinaturaExpertRepository.save(expert);
  }

  /**
   * Retorna as assinaturas com vencimento a partir da data indicada (inclusive).
   * 
   * @param data Data.
   * @return Lista de assinaturas.
   */
  public List<Assinatura> getList(LocalDate data) {
    return assinaturaRepository.getList(data);
  }

  public List<AssinaturaView> getListView() {
    return assinaturaRepository.getListView();
  }

  public Assinatura getByConta(String conta) {
    List<Assinatura> assinaturas = getList(conta);
    if (assinaturas.size() > 0)
      return assinaturas.get(0);
    return null;
  }

  public List<Assinatura> getListSubContas(Integer idAssinatura) {
    return assinaturaRepository.getListSubContas(idAssinatura);
  }

  /**
   * Realiza a associação e tratamento do pagamento na assinatura indicada.
   * 
   * @param associarRequest Requisição de associação.
   * @return Pagamento que representa a associação.
   */
  public AssinaturaPagamento associarTratarPagamento(
      AssociarTratarPagamentoRequest associarRequest) {
    AssinaturaPagamento retorno = null;
    Pagamento pagamento = pagamentoRepository.get(associarRequest.getIdPagamento());
    if (pagamento != null) {
      if (pagamento.isAssociado()) {
        throw new BusinessException("Este pagamento já está associado.");
      }
      Produto produto = produtoService.getByNome(pagamento.getProduto());
      if (produto != null) {
        if (produto.getTipo() == ProdutoTipo.ASSINATURA_MENSAL
            || produto.getTipo() == ProdutoTipo.ADESAO) {
          Assinatura assinatura = assinaturaRepository.get(associarRequest.getIdAssinatura());

          if (assinatura != null) {
            AssinaturaPagamento assPagamento = AssinaturaPagamento.builder().assinatura(assinatura)
                .dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build();
            assinaturaPagamentoRepository.save(assPagamento);
            retorno = assPagamento;
            pagamento.setAssociado(true);
            pagamentoRepository.save(pagamento);
            if (!associarRequest.getApenasAssociar()
                && produto.getTipo() == ProdutoTipo.ASSINATURA_MENSAL) {
              if (assinatura.getDataVencimento()
                  .compareTo(pagamento.getDataAtualizacao().toLocalDate()) > 0) {
                assinatura.setDataVencimento(assinatura.getDataVencimento().plusMonths(1));
              } else {
                assinatura
                    .setDataVencimento(pagamento.getDataAtualizacao().toLocalDate().plusMonths(1));
              }
              assinatura.setTelefone(pagamento.getTelefone());
              assinatura.setDocumentoPagamento(pagamento.getCpf());
              assinatura.setEmailPagamento(pagamento.getEmail());
            }
            assinaturaRepository.save(assinatura);
            System.out.println("Pagamento identificado.[" + associarRequest.getIdPagamento()
                + "] Assinatura encontrada. [" + assinatura.getConta().getId() + "]");

            if (!associarRequest.getApenasAssociar()) {
              // Atualizando subcontas
              List<Assinatura> assinaturaSubContas = getListSubContas(assinatura.getId());
              if (!Objects.isNull(assinaturaSubContas)) {
                for (Assinatura subAssinatura : assinaturaSubContas) {
                  subAssinatura.setDataVencimento(assinatura.getDataVencimento());
                  subAssinatura.setTelefone(pagamento.getTelefone());
                  subAssinatura.setDocumentoPagamento(pagamento.getCpf());
                  subAssinatura.setEmailPagamento(pagamento.getEmail());
                  assinaturaRepository.save(subAssinatura);
                }
              }
            }
          } else {
            throw new BusinessException(
                "Assinatura não encontrada. (" + associarRequest.getIdAssinatura() + ")");
          }
        }
      } else {
        throw new BusinessException("Produto não encontrado. (" + pagamento.getProduto() + ")");
      }
    }
    return retorno;
  }

  /**
   * Realiza a alteração da data de vencimento da assinatura e suas sub contas.
   * 
   * @param alterarVencimentoRequest Request para alteração de assintura.
   */
  public void alterarVencimento(AlterarVencimentoRequest alterarVencimentoRequest) {
    if (Objects.isNull(alterarVencimentoRequest.getNovoVencimento())) {
      throw new BusinessException("Novo vencimento é obrigatório.");
    }
    if (Strings.isEmpty(alterarVencimentoRequest.getMotivo())) {
      throw new BusinessException("Obrigatório informar o motivo.");
    }

    Assinatura assinatura = assinaturaRepository.get(alterarVencimentoRequest.getIdAssinatura());
    if (Objects.isNull(assinatura)) {
      throw new BusinessException(
          "Assinatura não encontrada. (" + alterarVencimentoRequest.getIdAssinatura() + ")");
    }

    assinatura.setDataVencimento(alterarVencimentoRequest.getNovoVencimento());
    assinaturaRepository.save(assinatura);

    // Alteração da data de vencimento de subcontas.
    List<Assinatura> assinaturaSubContas = getListSubContas(assinatura.getId());
    if (!Objects.isNull(assinaturaSubContas)) {
      for (Assinatura subAssinatura : assinaturaSubContas) {
        subAssinatura.setDataVencimento(assinatura.getDataVencimento());
        assinaturaRepository.save(subAssinatura);
      }
    }
  }

  /**
   * Realiza a alteração do email da assinatura.
   * 
   * @param alterarEmailRequest Request para alteração de email.
   */
  public void alterarEmail(AlterarEmailRequest alterarEmailRequest) {
    Assinatura assinatura = assinaturaRepository.get(alterarEmailRequest.getIdAssinatura());
    if (Objects.isNull(alterarEmailRequest.getNovoEmail())) {
      throw new BusinessException("Novo email não informado.");
    }
    if (Objects.isNull(assinatura)) {
      throw new BusinessException(
          "Assinatura não encontrada. (" + alterarEmailRequest.getIdAssinatura() + ")");
    }
    assinatura.setEmailPagamento(alterarEmailRequest.getNovoEmail().trim().toLowerCase());
    assinaturaRepository.save(assinatura);
  }

  /**
   * Retorna o pagamento com o ID indicado.
   * 
   * @param idPagamento ID do pagamento.
   * @return Pagamento com o ID indicado.
   */
  public AssinaturaPagamento getPagamento(Integer idPagamento) {
    if (Objects.isNull(idPagamento))
      throw new BusinessException("ID do pagamento não informado.");
    return assinaturaPagamentoRepository.getById(idPagamento);
  }

  /**
   * Realiza a exclusão do pagamento indicado.
   * 
   * @param excluirPagamentoRequest Request para exclusão de pagamento.
   */
  public void excluirPagamento(ExcluirAssinaturaPagamentoRequest excluirPagamentoRequest) {
    if (Objects.isNull(excluirPagamentoRequest))
      throw new BusinessException("Request de excluir pagamento vazia.");
    if (Objects.isNull(excluirPagamentoRequest.getId()))
      throw new BusinessException("ID do pagamento não informado.");
    if (Objects.isNull(excluirPagamentoRequest.getIdAssinatura()))
      throw new BusinessException("ID da assinatura não informado.");

    AssinaturaPagamento assinaturaPagamento =
        assinaturaPagamentoRepository.getById(excluirPagamentoRequest.getId());
    if (Objects.isNull(assinaturaPagamento)) {
      throw new BusinessException("Pagamento não encontrado.");
    }

    if (!excluirPagamentoRequest.getIdAssinatura()
        .equals(assinaturaPagamento.getAssinatura().getId())) {
      throw new BusinessException("O pagamento não corresponde a assinatura indicada.");
    }

    Pagamento pagamento = pagamentoRepository.get(assinaturaPagamento.getPagamento().getId());

    Produto produto = produtoService.getByNome(pagamento.getProduto());
    if (produto != null) {
      // Produto identificado.
      if (produto.getTipo() == ProdutoTipo.ASSINATURA_MENSAL) {
        Assinatura assinatura =
            assinaturaRepository.get(assinaturaPagamento.getAssinatura().getId());
        assinatura.setDataVencimento(assinatura.getDataVencimento().minusMonths(1));
        assinaturaRepository.save(assinatura);
      }
    } else {
      // Tipo do produto não identificado.
    }
    pagamento.setAssociado(false);
    pagamentoRepository.save(pagamento);

    assinaturaPagamentoRepository.delete(assinaturaPagamento);
  }

  public void excluirExpert(Integer idAssinaturaExpert) {
    AssinaturaExpert assinaturaExpert = assinaturaExpertRepository.get(idAssinaturaExpert);
    if (Objects.isNull(assinaturaExpert))
      throw new BusinessException("Expert não encontrado. [" + idAssinaturaExpert + "]");
    assinaturaExpertRepository.delete(assinaturaExpert);
  }

  public void adicionarExpert(AdicionarExpertAssinaturaRequest adicionarExpertRequest) {
    // TODO Auto-generated method stub
    Robo robo = roboRepository.getById(adicionarExpertRequest.getExpert());
    Assinatura assinatura = assinaturaRepository.get(adicionarExpertRequest.getIdAssinatura());
    AssinaturaExpert assinaturaExpert =
        AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(robo)
            .volume(Double.parseDouble(adicionarExpertRequest.getAlavancagem() + ""))
            .volumeMaximo(Double.parseDouble(adicionarExpertRequest.getAlavancagemMaxima() + ""))
            .build();
    assinaturaExpertRepository.save(assinaturaExpert);

  }

  /**
   * Desabilita a assinatura indicada.
   * 
   * @param idAssinatura ID da assinatura.
   */
  public void desabilitar(Integer idAssinatura) {
    Assinatura assinatura = assinaturaRepository.get(idAssinatura);
    if (assinatura == null) {
      throw new BusinessException("Assinatura não encontrada. [" + idAssinatura + "]");
    }
    assinatura.setDesabilitada(true);
    gravar(assinatura);
  }

  /**
   * Habilita a assinatura indicada.
   * 
   * @param idAssinatura ID da assinatura.
   */
  public void habilitar(Integer idAssinatura) {
    Assinatura assinatura = assinaturaRepository.get(idAssinatura);
    if (assinatura == null) {
      throw new BusinessException("Assinatura não encontrada. [" + idAssinatura + "]");
    }
    assinatura.setDesabilitada(false);
    gravar(assinatura);
  }

  public List<AssinaturaView> getInativas(Afiliado afiliado, boolean contabilizarResultados) {
    if (Objects.isNull(afiliado)) {
      return new ArrayList<>();
    }
    List<AssinaturaView> assinaturas =
        assinaturaRepository.getListInativasView(afiliado.getId(), LocalDate.now());
    if (contabilizarResultados) {
      Map<String, AssinaturaView> mapaAssinaturas = new HashMap<>();
      for (AssinaturaView assinatura : assinaturas)
        mapaAssinaturas.put(assinatura.getConta(), assinatura);
      List<AssinaturaResultadoView> assinaturaResultados =
          assinaturaRepository.getListResultadosView(afiliado.getId());
      for (AssinaturaResultadoView assinaturaResultado : assinaturaResultados) {
        AssinaturaView assinatura = mapaAssinaturas.get(assinaturaResultado.getConta());
        if (!Objects.isNull(assinatura))
          assinatura.setResultado(assinaturaResultado.getResultado());
      }
    }
    return assinaturas;
  }

  /**
   * Retorna a lista de assinaturas do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @return Lista de assinaturas.
   */
  public List<Assinatura> getList(Afiliado afiliado) {
    if (Objects.isNull(afiliado))
      return new ArrayList<>();
    return assinaturaRepository.getList(afiliado);
  }


  public Assinatura ativarAssinatura(AtivarAssinaturaRequest ativarRequest, Afiliado afiliado) {
    if (Objects.isNull(ativarRequest.getIdConta()))
      throw new BusinessException("A conta não foi especificada.");
    if (Objects.isNull(ativarRequest.getIdExpert()))
      throw new BusinessException("A automação não foi especificada.");
    if (Objects.isNull(ativarRequest.getIdPagamento()))
      throw new BusinessException("O pagamento não foi especificado.");

    Conta conta = contaRepository.get(ativarRequest.getIdConta());
    if (Objects.isNull(conta)) {
      throw new BusinessException("Conta não existe.");
    }

    Pagamento pagamento = null;
    if (ativarRequest.getIdPagamento() > 0) {
      pagamento = pagamentoRepository.get(ativarRequest.getIdPagamento(), afiliado);
      if (Objects.isNull(pagamento)) {
        throw new BusinessException("Pagamento não encontrado.");
      }
    }

    Robo automacao = roboRepository.get(ativarRequest.getIdExpert(), afiliado);
    if (Objects.isNull(automacao)) {
      throw new BusinessException("Automação não encontrada.");
    }
    if (!automacao.getEnabled()) {
      throw new BusinessException("Automação não está ativada.");
    }

    Assinatura assinatura =
        Assinatura.builder().afiliado(afiliado).conta(conta).dataCadastro(LocalDateTime.now())
            .dataVencimento(pagamento != null ? LocalDate.now().plusMonths(1) : LocalDate.now())
            .documentoPagamento(
                (pagamento != null ? DocumentoFormatter.format(pagamento.getCpf()) : null))
            .emailPagamento(pagamento != null ? pagamento.getEmail().toLowerCase() : null)
            .telefone(pagamento != null ? pagamento.getTelefone() : null).pendente(false)
            .servidor(getServidorParaAlocacao()).pausado(true).build();
    assinaturaRepository.save(assinatura);

    // Expert
    AssinaturaExpert expert =
        AssinaturaExpert.builder().assinatura(assinatura).ativado(true).expert(automacao)
            .volume(automacao.getVolume()).volumeMaximo(automacao.getVolume()).build();

    assinaturaExpertRepository.save(expert);

    if (pagamento != null) {
      assinaturaPagamentoRepository.save(AssinaturaPagamento.builder().assinatura(assinatura)
          .dataDeCadastro(LocalDateTime.now()).pagamento(pagamento).build());


      pagamento.setAssociado(true);
      pagamentoRepository.save(pagamento);
    }
    return assinatura;
  }

  public Assinatura get(String idConta, Afiliado afiliado) {
    return assinaturaRepository.getByContaAfiliado(idConta, afiliado);
  }
}
