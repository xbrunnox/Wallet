package br.app.grid.wallet.operacao;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String licenca;
	
	private LocalDateTime dataDeEntrada;
	
	private LocalDateTime dataDeSaida;
	
	private String ativo;
	
	private Double volume;
	
	private Double entrada;
	
	private Double saida;
	
	private Double pontos;
	
	private Double valor;
	
	private String direcao;

}
