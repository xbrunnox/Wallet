package br.app.grid.wallet.assinatura;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.licenca.Conta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assinatura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;

	private LocalDateTime dataCadastro;

	private LocalDate dataVencimento;
	
	private String telefone;
	
	private String documentoPagamento;
	
	private String emailPagamento;
	
	private boolean pausado;

}
