package br.app.grid.wallet.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.app.grid.wallet.log.LogComando;
import br.app.grid.wallet.log.LogComandoService;
import br.app.grid.wallet.socket.Util;
import br.app.grid.wallet.web.request.FiltrarLogsRequest;
import br.app.grid.wallet.web.request.GravarLogComandoRequest;

@RestController
@RequestMapping("/log")
public class LogController {

	@Autowired
	private LogComandoService logComandoService;

	@PostMapping("/comando")
	public void registrar(@RequestBody GravarLogComandoRequest request) {
		logComandoService.gravar(LogComando.builder().comando(request.getComando()).conta(request.getConta())
				.dataHora(request.getDataHora()).data(request.getDataHora().toLocalDate()).build());
	}
	
	@PostMapping("/filtrar")
	public List<LogComando> filtrarLogs(@RequestBody FiltrarLogsRequest filtrarLogsRequest) {
	  Util.println(filtrarLogsRequest);
	  return logComandoService.filtrar(filtrarLogsRequest);
	  
	}
}
