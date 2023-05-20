package br.app.grid.wallet;

import java.util.List;
import br.app.grid.wallet.backtest.Backtest;
import br.app.grid.wallet.backtest.BacktestOperacao;
import br.app.grid.wallet.util.ExcelImport;
import br.app.grid.wallet.util.VersatilUtil;

public class PoiTest {

  public static void main(String[] args) {
    Backtest backtest = new Backtest();
    List<BacktestOperacao> operacoes = ExcelImport.importBacktestOperacoes(backtest, "Tester.xlsx");
    System.out.println(VersatilUtil.toJson(operacoes));
  }

}
