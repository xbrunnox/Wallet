package br.app.grid.wallet.assinatura.view;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="assinatura_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaView {
	
	@Id
	private Integer id;
	
	private String conta;
	
	private String nome;
	
	private String corretora;
	
	private String maquina;
	
	private String servidor;
	
	private LocalDate dataVencimento;

}
