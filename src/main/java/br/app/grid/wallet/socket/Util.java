package br.app.grid.wallet.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.app.grid.wallet.web.request.FiltrarLogsRequest;

public class Util {

	public static void sleep(int timeInMilliSeconds) {
		try {
			Thread.sleep(timeInMilliSeconds);
		} catch (InterruptedException e) {
		}
	}

  public static void println(FiltrarLogsRequest filtrarLogsRequest) {
    try {
      System.out.println(new ObjectMapper().writeValueAsString(filtrarLogsRequest));
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
