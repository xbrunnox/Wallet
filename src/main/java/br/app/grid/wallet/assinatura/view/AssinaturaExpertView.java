package br.app.grid.wallet.assinatura.view;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AssinaturaExpertView {
	
	@Id
	private int id;
	
	private int idAssinatura;
	
	private String conta;
	
	private String expert;
	
	private String ativo;
	
	private Double volume;
	
	private LocalDate dataVencimento;
	
	private boolean ativado;

}
