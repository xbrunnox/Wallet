package br.app.grid.wallet.pagamento;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;

@Service
public class PagamentoService {

  @Autowired
  private PagamentoRepository repository;

  public void gravar(Pagamento pagamento) {
    repository.save(pagamento);
  }

  public List<Pagamento> getListNaoAssociados() {
    return repository.getListNaoAssociados();
  }

  public List<Pagamento> getList() {
    return repository.getList();
  }

  public Pagamento get(Long id) {
    return repository.get(id);
  }

  public List<Pagamento> getList(Afiliado afiliado) {
    return repository.getList(afiliado);
  }

  public List<Pagamento> getListNaoAssociados(Afiliado afiliado) {
    return repository.getListNaoAssociado(afiliado);
  }

  public List<Pagamento> getListNaoAssociados(Afiliado afiliado, int numeroDeRegistros) {
    List<Pagamento> pagamentos = repository.getListNaoAssociado(afiliado);
    Collections.sort(pagamentos, new Comparator<Pagamento>() {
      @Override
      public int compare(Pagamento o1, Pagamento o2) {
        if (o1.getId() < o2.getId())
          return 1;
        if (o1.getId() > o2.getId())
          return -1;
        return 0;
      }
    });

    if (pagamentos.size() > numeroDeRegistros) {
      return pagamentos.subList(0, numeroDeRegistros);
    }

    return pagamentos;
  }

}
