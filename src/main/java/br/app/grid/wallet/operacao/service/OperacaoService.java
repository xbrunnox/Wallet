package br.app.grid.wallet.operacao.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.afiliado.AfiliadoRepository;
import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.ativo.AtivoRepository;
import br.app.grid.wallet.licenca.Conta;
import br.app.grid.wallet.licenca.ContaRepository;
import br.app.grid.wallet.operacao.Operacao;
import br.app.grid.wallet.operacao.TipoOperacao;
import br.app.grid.wallet.operacao.repository.OperacaoRepository;
import br.app.grid.wallet.robo.Robo;
import br.app.grid.wallet.robo.RoboRepository;
import br.app.grid.wallet.trade.Trade;
import br.app.grid.wallet.trade.repository.TradeRepository;
import br.app.grid.wallet.web.request.GravarOperacaoRequest;

@Service
public class OperacaoService {

  @Autowired
  private OperacaoRepository repository;

  @Autowired
  private AtivoRepository ativoRepository;
  
  @Autowired
  private AfiliadoRepository afiliadoRepository;

  @Autowired
  private ContaRepository contaRepository;

  @Autowired
  private RoboRepository expertRepository;

  @Autowired
  private TradeRepository tradeRepository;

  public Operacao save(Operacao operacao) {
    repository.save(operacao);
    return operacao;
  }

  public Operacao save(GravarOperacaoRequest request) {
    Ativo ativo = ativoRepository.findByCodigo(request.getAtivo());
    Conta conta = contaRepository.findById(request.getConta()).orElse(null);

    Robo robo = expertRepository.findById(request.getExpert()).orElse(null);
    Afiliado afiliado = afiliadoRepository.get(request.getIdAfiliado());
    System.out.println("Pesqusiando EXPERT: " + request.getExpert() + " " + robo);
    if (request.getTipo().equals(TipoOperacao.ENTRADA)) {
      // Entrada
      Operacao operacaoEntrada =
          Operacao.builder().ativo(ativo).conta(conta).afiliado(afiliado)
              .direcao(request.getDirecao()).expert(robo).preco(request.getPreco())
              .tipo(request.getTipo()).volume(request.getVolume()).data(request.getData()).build();
      save(operacaoEntrada);
    } else if (request.getTipo().equals(TipoOperacao.SAIDA)) {
      // Saída
      Operacao operacaoSaida =
          Operacao.builder().ativo(ativo).conta(conta).afiliado(afiliado)
              .direcao(request.getDirecao()).expert(robo).preco(request.getPreco())
              .tipo(request.getTipo()).volume(request.getVolume()).data(request.getData()).build();
      save(operacaoSaida);
    }
    checarOperacoes(request.getConta());
    return null;
  }

  public void checarOperacoes(String account) {
    List<Operacao> compras = getList(account, "C");
    List<Operacao> vendas = getList(account, "V");
    List<Trade> trades = new ArrayList<>();

    for (Operacao compra : compras) {
      if (compra.getVolume().compareTo(BigDecimal.ZERO) > 0) {
        for (Operacao venda : vendas) {
          // Verifica se os ativos são iguais
          if (compra.getAtivo().getCodigo().equals(venda.getAtivo().getCodigo())) {
            // Verifica se o volume da venda e da compra são maiores que zero.
            if (venda.getVolume().compareTo(BigDecimal.ZERO) > 0
                && compra.getVolume().compareTo(BigDecimal.ZERO) > 0) {
              // Verifica se os volumes são iguais
              if (compra.getVolume().compareTo(venda.getVolume()) == 0) {
                String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
                Double pontos = venda.getPreco() - compra.getPreco();
                Double resultado = pontos * compra.getAtivo().getCategoria().getGanho()
                    * compra.getVolume().doubleValue();

                LocalDateTime dataEntrada = null;
                LocalDateTime dataSaida = null;
                Long duracao = null;
                if (compra.getData().compareTo(venda.getData()) < 0) {
                  duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
                  dataEntrada = compra.getData();
                  dataSaida = venda.getData();
                } else {
                  duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
                  dataEntrada = venda.getData();
                  dataSaida = compra.getData();
                }

                Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
                    .afiliado(compra.getAfiliado()).conta(compra.getConta())
                    .dataEntrada(dataEntrada).dataSaida(dataSaida).direcao(direcao).duracao(duracao)
                    .expert(!Objects.isNull(compra.getExpert()) ? compra.getExpert()
                        : venda.getExpert())
                    .pontos(pontos).resultado(resultado).venda(venda.getPreco())
                    .volume(compra.getVolume()).build();
                trades.add(trade);
                compra.setVolume(BigDecimal.ZERO);
                venda.setVolume(BigDecimal.ZERO);
                // Verifica se o volume de compra é maior que o de venda
              } else if (compra.getVolume().compareTo(venda.getVolume()) > 0) {
                BigDecimal quantidade = venda.getVolume();
                String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
                Double pontos = venda.getPreco() - compra.getPreco();
                Double resultado =
                    pontos * compra.getAtivo().getCategoria().getGanho() * quantidade.doubleValue();

                LocalDateTime dataEntrada = null;
                LocalDateTime dataSaida = null;
                Long duracao = null;
                if (compra.getData().compareTo(venda.getData()) < 0) {
                  duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
                  dataEntrada = compra.getData();
                  dataSaida = venda.getData();
                } else {
                  duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
                  dataEntrada = venda.getData();
                  dataSaida = compra.getData();
                }

                Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
                    .afiliado(compra.getAfiliado()).conta(compra.getConta())
                    .dataEntrada(dataEntrada).dataSaida(dataSaida).direcao(direcao).duracao(duracao)
                    .expert(!Objects.isNull(compra.getExpert()) ? compra.getExpert()
                        : venda.getExpert())
                    .pontos(pontos).resultado(resultado).venda(venda.getPreco()).volume(quantidade)
                    .build();
                trades.add(trade);
                compra.setVolume(compra.getVolume().subtract(quantidade));
                venda.setVolume(venda.getVolume().subtract(quantidade));
                // Verifica se o volume de venda é maior que o de compra.
              } else if (compra.getVolume().compareTo(venda.getVolume()) < 0) {
                BigDecimal quantidade = compra.getVolume();
                String direcao = compra.getData().compareTo(venda.getData()) < 0 ? "C" : "V";
                Double pontos = venda.getPreco() - compra.getPreco();
                Double resultado =
                    pontos * compra.getAtivo().getCategoria().getGanho() * quantidade.doubleValue();

                LocalDateTime dataEntrada = null;
                LocalDateTime dataSaida = null;

                Long duracao = null;
                if (compra.getData().compareTo(venda.getData()) < 0) {
                  duracao = ChronoUnit.SECONDS.between(compra.getData(), venda.getData());
                  dataEntrada = compra.getData();
                  dataSaida = venda.getData();
                } else {
                  duracao = ChronoUnit.SECONDS.between(venda.getData(), compra.getData());
                  dataEntrada = venda.getData();
                  dataSaida = compra.getData();
                }

                Trade trade = Trade.builder().ativo(compra.getAtivo()).compra(compra.getPreco())
                    .afiliado(compra.getAfiliado()).conta(compra.getConta())
                    .dataEntrada(dataEntrada).dataSaida(dataSaida).direcao(direcao).duracao(duracao)
                    .expert(!Objects.isNull(compra.getExpert()) ? compra.getExpert()
                        : venda.getExpert())
                    .pontos(pontos).resultado(resultado).venda(venda.getPreco()).volume(quantidade)
                    .build();
                trades.add(trade);
                compra.setVolume(compra.getVolume().subtract(quantidade));
                venda.setVolume(venda.getVolume().subtract(quantidade));
              }
            }
          }
        }
      }
    }

    for (Trade trade : trades) {
      tradeRepository.save(trade);
    }
    for (Operacao operacao : compras) {
      if (operacao.getVolume().compareTo(BigDecimal.ZERO) == 0) {
        excluir(operacao);
      }
    }
    for (Operacao operacao : vendas) {
      if (operacao.getVolume().compareTo(BigDecimal.ZERO) == 0) {
        excluir(operacao);
      }
    }
  }

  public List<Operacao> getList() {
    return repository.getList();
  }

  public Operacao get(int id) {
    return repository.get(id);
  }

  public List<Operacao> getList(String conta) {
    return repository.getList(conta);
  }

  public List<Operacao> getList(String conta, String direcao) {
    return repository.getList(conta, direcao);
  }

  public void excluir(Operacao operacao) {
    repository.delete(operacao);
  }

  public List<Operacao> getList(String conta, LocalDate data) {
    System.out.println(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    return repository.getList(conta,
        Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  /**
   * Retorna a lista de operações do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @return Lista de operações do afiliado.
   */
  public List<Operacao> getList(Afiliado afiliado) {
    if (Objects.isNull(afiliado))
      return new ArrayList<>();
    return repository.getList(afiliado);
  }

}
