package br.app.grid.wallet.cotacao.service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import br.app.grid.wallet.ativo.Ativo;
import br.app.grid.wallet.ativo.AtivoService;
import br.app.grid.wallet.candle.Candle;
import br.app.grid.wallet.candle.service.CandleService;
import br.app.grid.wallet.cotacao.Cotacao;
import br.app.grid.wallet.cotacao.CotacaoVO;
import br.app.grid.wallet.cotacao.repository.CotacaoRepository;
import br.app.grid.wallet.enums.TimeFrameEnum;
import br.app.grid.wallet.util.HttpUtil;
import br.app.grid.wallet.web.request.RegistrarCotacaoRequest;
import br.app.grid.wallet.web.response.CotacaoResponse;

@Service
public class CotacaoService {

  @Autowired
  private CotacaoRepository repository;

  @Autowired
  private AtivoService ativoService;

  @Autowired
  private CandleService candleService;

  private Map<String, CotacaoResponse> mapaCotacoes = new HashMap<>();

  public void atualizar() {
    List<CotacaoVO> cotacoes = consultarCotacoes();
    for (CotacaoVO cotacao : cotacoes) {
      Ativo ativo = ativoService.get(cotacao.getCodigo());
      if (ativo == null) {
        // ativo = Ativo.builder().codigo(cotacao.getCodigo()).nome(cotacao.getNome()).build();
        ativoService.salvar(ativo);
      }
      Cotacao ultimaCotacao = getUltimaCotacao(ativo);
      if (ultimaCotacao == null) {
        ultimaCotacao = Cotacao.builder().ativo(ativo).data(LocalDateTime.now())
            .valor(BigDecimal.valueOf(cotacao.getValor())).build();
        repository.save(ultimaCotacao);
      } else {
        ultimaCotacao.setValor(BigDecimal.valueOf(cotacao.getValor()));
        repository.save(ultimaCotacao);
      }
    }

  }

  public Cotacao getUltimaCotacao(Ativo ativo) {
    return null;
    // return repository.findFirstByIdAtivo(ativo.getId());
  }

  private List<CotacaoVO> consultarCotacoes() {
    String csv = HttpUtil.get(
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vQY5tf1PV7pmj8qZpURLxZzgnEbt53MRdPUB_W4didtSAXbp2lBv5gZDWfxWmfFLUMzduURXQeN3ewV/pub?output=csv");
    System.out.println(csv);
    List<CotacaoVO> cotacoes = new ArrayList<>();
    int linha = 1;
    try (CSVReader csvReader = new CSVReader(new StringReader(csv));) {
      String[] values = null;
      while ((values = csvReader.readNext()) != null) {
        if (linha > 1 && values.length >= 3) {
          cotacoes.add(CotacaoVO.builder().codigo(values[0])
              .valor(NumberFormat.getNumberInstance(Locale.FRANCE).parse(values[1]).doubleValue())
              .nome(values[2]).build());
        }
        System.out.println(linha);
        linha++;
      }
      System.out.println(cotacoes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cotacoes;
  }

  /**
   * Retorna a cotação pro ativo indicado.
   * 
   * @param ativo Ativo.
   * @return Cotação.
   */
  public CotacaoResponse getCotacao(String ativo) {
    CotacaoResponse cotacao = mapaCotacoes.get(ativo);
    if (Objects.isNull(cotacao)) {
      Candle candle = candleService.getUltimoCandle(ativo, TimeFrameEnum.M1);
      if (Objects.isNull(candle))
        return null;
      cotacao = CotacaoResponse.builder().ativo(ativo).data(candle.getDataHora())
          .preco(candle.getFechamento()).build();
    }
    return cotacao;
  }

  /**
   * Realiza o registro da cotação em memória.
   * 
   * @param request Request.
   * @return Cotação.
   */
  public CotacaoResponse registrarCotacao(RegistrarCotacaoRequest request) {
    CotacaoResponse cotacao = CotacaoResponse.builder().ativo(request.getAtivo())
        .data(LocalDateTime.now()).preco(request.getPreco()).build();
    mapaCotacoes.put(request.getAtivo(), cotacao);
    return cotacao;
  }

}
