package br.app.grid.wallet.web.response;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResumo {

  private Integer numeroClientesOnline;

  private Integer numeroExpertsOnline;

  private Integer numeroServidoresOnline;
  
  private Integer numeroEmOperacao;

  private List<String> experts;

  private Set<String> clientes;

  private Set<String> servidores;
  
  private String data;
  
  private String horario;

}
