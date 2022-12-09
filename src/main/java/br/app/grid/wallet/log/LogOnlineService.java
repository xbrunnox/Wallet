package br.app.grid.wallet.log;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogOnlineService {

	@Autowired
	private LogOnlineRepository repository;

	public void gravar(LogOnline log) {
		repository.save(log);
	}

	public List<LogOnline> getList() {
		return repository.getList();
	}

	public List<LogOnline> getList24Horas() {
		List<LogOnline> retorno = repository.findTop288ByOrderByHorarioDesc();
		Collections.reverse(retorno);
		return retorno;
	}

}
