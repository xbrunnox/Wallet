package br.app.grid.wallet.afiliado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>AfiliadoLojaService</b><br>
 * Service responsável pelas operações da entidade AfiliadoLoja.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 03 de março de 2024.
 */
@Service
public class AfiliadoLojaService {

  @Autowired
  private AfiliadoLojaRepository repository;

  /**
   * Retorna o afiliado ao qual pertence a loja com o ID de Loja indicado.
   * 
   * @param idLoja ID da loja.
   * @return Afiliado.
   */
  public Afiliado getAfiliadoByIdLoja(String idLoja) {
    AfiliadoLoja afiliadoLoja = repository.getByIdLoja(idLoja);
    return (afiliadoLoja != null ? afiliadoLoja.getAfiliado() : null);
  }

}
