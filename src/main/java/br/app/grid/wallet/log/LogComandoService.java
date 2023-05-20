package br.app.grid.wallet.log;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.web.request.FiltrarLogsRequest;

/**
 * <b>LogComandoService</b><br>
 * Service responsável pelas operações com a entidade LogComando.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 28 de janeiro de 2023.
 */
@Service
public class LogComandoService {

  @Autowired
  private LogComandoRepository repository;

  /**
   * Realiza a gravação do log.
   * 
   * @param logComando Log de comando.
   * @return Log após a gravação.
   */
  public LogComando gravar(LogComando logComando) {
    repository.save(logComando);
    return logComando;
  }

  /**
   * Retorna a lista de logs do dia.
   * 
   * @return Lista de logs do dia.
   */
  public List<LogComando> getListDia() {
    return repository.getList(LocalDate.now());
  }

  /**
   * Retorna a lista de logs que correspondam ao filtro indicado.
   * 
   * @param request Request (Filtro).
   * @return Lista de log.
   */
  public List<LogComando> filtrar(FiltrarLogsRequest request) {
    if (Strings.isBlank(request.getConta1()))
      request.setConta1(null);
    if (Strings.isBlank(request.getConta2()))
      request.setConta2(null);
    if (Strings.isBlank(request.getConteudo()))
      request.setConteudo(null);
    else
      request.setConteudo("%" + request.getConteudo().replaceAll(" ", "%") + "%");

    return repository.getList(request.getDataInicial(), request.getDataFinal(), request.getConta1(),
        request.getConta2(), request.getConteudo());
  }
}
