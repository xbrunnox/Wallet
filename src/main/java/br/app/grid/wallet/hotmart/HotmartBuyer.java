package br.app.grid.wallet.hotmart;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Br+unno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class HotmartBuyer {
  
  private String email;
  
  private String name;
  
  @JsonAlias({"checkout_phone"})
  private String phone;
  
  /**
  "buyer": {
    "email": "buyer@email.com",
    "name": "Buyer name",
    "checkout_phone": "999999999"
  },
  */

}
