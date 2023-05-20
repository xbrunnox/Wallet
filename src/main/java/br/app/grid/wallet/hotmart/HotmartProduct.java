package br.app.grid.wallet.hotmart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 27 de abril de 2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotmartProduct {
  
  private Integer id;
  
  private String ucode;
  
  private String name;
  
  private boolean hasCoProduction;
  
  /**
   *  "id": 213344,
        "ucode": "2e9c43a9-0aeb-48ed-9464-630f845c23af",
        "name": "Product Name",
        "has_co_production": false
   */

}
