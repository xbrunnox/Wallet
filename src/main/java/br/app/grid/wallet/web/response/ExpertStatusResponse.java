package br.app.grid.wallet.web.response;

import java.util.List;
import br.app.grid.wallet.client.ExpertUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 24 de janeiro de 2024.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertStatusResponse {
  
  private List<ExpertUser> onlineExperts;

}
