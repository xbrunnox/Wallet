package br.app.grid.wallet.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.backtest.service.BacktestOperacaoService;
import br.app.grid.wallet.backtest.service.BacktestService;
import br.app.grid.wallet.backtest.vo.BacktestDiaVO;
import br.app.grid.wallet.backtest.vo.BacktestMesAnoVO;
import br.app.grid.wallet.util.ExcelImport;
import br.app.grid.wallet.util.VersatilUtil;
import br.app.grid.wallet.web.request.BacktestImportarRequest;

@Controller
@RequestMapping("/backtest")
public class BacktestController {

  @Autowired
  private BacktestService backtestService;
  @Autowired
  private BacktestOperacaoService backtestOperacaoService;

  @GetMapping("")
  public ModelAndView get() {
    List<Backtest> backtests = backtestService.getList();
    ModelAndView modelAndView = new ModelAndView("backtest/backtest-selecao");
    modelAndView.addObject("backtestList", backtests);
    return modelAndView;
  }

  @GetMapping("/{idBacktest}/{idBacktest2}/{volume1}/{volume2}")
  public ModelAndView get(@PathVariable("idBacktest") Integer idBacktest,
      @PathVariable("idBacktest2") Integer idBacktest2, @PathVariable("volume1") Integer volume1,
      @PathVariable("volume2") Integer volume2) {
    Backtest backtest = backtestService.get(idBacktest);
    Backtest backtest2 = backtestService.get(idBacktest2);
    List<BacktestMesAnoVO> retorno =
        backtestOperacaoService.getAcumulado(backtest, BigDecimal.valueOf(volume1));
    List<BacktestMesAnoVO> retorno2 =
        backtestOperacaoService.getAcumulado(backtest2, BigDecimal.valueOf(volume2));
    System.out.println("Terminou a consulta");
    BigDecimal resultado1 = BigDecimal.ZERO;
    BigDecimal resultado2 = BigDecimal.ZERO;
    for (BacktestMesAnoVO mes : retorno) {
      resultado1 = resultado1.add(mes.getTotal());
    }
    for (BacktestMesAnoVO mes : retorno2) {
      resultado2 = resultado2.add(mes.getTotal());
    }
    ModelAndView modelAndView = new ModelAndView("backtest/backtest-comparar");
    modelAndView.addObject("meses1", retorno);
    modelAndView.addObject("meses2", retorno2);
    modelAndView.addObject("resultado1", resultado1);
    modelAndView.addObject("resultado2", resultado2);
    return modelAndView;
  }

  @GetMapping("/detalhes/{idBacktest}/{mes}/{ano}")
  public @ResponseBody ModelAndView detalhes(@PathVariable("idBacktest") Integer idBacktest,
      @PathVariable("mes") Integer mes, @PathVariable("ano") Integer ano) {
    LocalDate dataInicial = LocalDate.of(ano, mes, 1);
    LocalDate dataFinal = dataInicial.plusMonths(1).minusDays(1);
    Backtest backtest = backtestService.get(idBacktest);
    List<BacktestDiaVO> detalhes =
        backtestOperacaoService.getDetalhesPorDia(idBacktest, dataInicial, dataFinal);

    ModelAndView modelAndView = new ModelAndView("backtest/detalhes");
    modelAndView.addObject("dias", detalhes);
    modelAndView.addObject("backtest", backtest);
    return modelAndView;
  }

  @GetMapping("/importar")
  public ModelAndView importar() {
    List<Backtest> backtests = backtestService.getList();
    ModelAndView modelAndView = new ModelAndView("backtest/importar");
    modelAndView.addObject("backtestList", backtests);
    return modelAndView;
  }

  @PostMapping("/importar")
  public void importar(BacktestImportarRequest request) {
    System.out.println(request.getIdBacktest());
    System.out.println(request.getTransacoes());
    backtestService.importar2(request.getIdBacktest(), request.getTransacoes());
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      @RequestParam("idBacktest") Integer idBacktest, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + " to backtest " + idBacktest);
    System.out.println(
        "You successfully uploaded " + file.getOriginalFilename() + " to backtest " + idBacktest);
    Backtest backtest = backtestService.get(idBacktest);
    try {
      List<BacktestOperacao> operacoes =
          ExcelImport.importBacktestOperacoes(backtest, file.getInputStream());
      if (operacoes.size() > 0)
        backtestOperacaoService.gravar(operacoes);
      System.out.println(VersatilUtil.toJson(operacoes));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return "redirect:/";
  }

}
