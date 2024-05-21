package br.app.grid.wallet.robo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;

@Service
public class RoboService {

  @Autowired
  private RoboRepository repository;

  public Robo getById(String id) {
    return repository.findById(id).get();
  }

  public List<Robo> getListEnabled() {
    return repository.getListEnabled();
  }

  /**
   * Retorna a lista de automações habilitadas do afiliado indicado.
   * 
   * @param afiliado Afiliado.
   * @return Lista de automações.
   */
  public List<Robo> getListEnabled(Afiliado afiliado) {
    return repository.getListEnabled(afiliado);
  }

}
