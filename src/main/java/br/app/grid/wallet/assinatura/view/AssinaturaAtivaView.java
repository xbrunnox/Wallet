package br.app.grid.wallet.assinatura.view;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaAtivaView {
	
	@Id
	private int id;
	
	private String conta;
	
	private String nome;
	
	private LocalDate dataVencimento;
	
	private String corretora;
	
	private String observacao;

}
