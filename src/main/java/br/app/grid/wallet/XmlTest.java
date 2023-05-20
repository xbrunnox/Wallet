package br.app.grid.wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import br.app.grid.wallet.util.VersatilUtil;

public class XmlTest {
  
  public static void main(String[] args) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("Optimizer.xml"));
      String texto = "";
      String linha = br.readLine();
      while (linha != null) {
        texto += linha;
        linha = br.readLine();
      }
      br.close();
      System.out.println(VersatilUtil.xmlToJson(texto, 3));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
