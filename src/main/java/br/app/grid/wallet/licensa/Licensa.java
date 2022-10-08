package br.app.grid.wallet.licensa;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Licensa {
	
	@Id
	private String id;
	
	private String nome;
	
	private String corretora;
	
	private Integer conta;
	
	private LocalDate vencimento;
	
	private LocalDateTime dataDeCadastro;

}
