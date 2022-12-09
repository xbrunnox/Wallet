package br.app.grid.wallet.log;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogComandoService {
	
	@Autowired
	private LogComandoRepository repository;
	
	public LogComando gravar(LogComando logComando) {
		repository.save(logComando);
		return logComando;
	}

	public List<LogComando> getListDia() {
		return repository.getList(LocalDate.now());
	}

}