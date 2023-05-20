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
import br.app.grid.wallet.maquina.Maquina;
import br.app.grid.wallet.servidor.Servidor;
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
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;

	@ManyToOne
	@JoinColumn(name = "servidor")
	private Servidor servidor;
	
	@ManyToOne
	@JoinColumn(name = "maquina")
	private Maquina maquina;
	
	@ManyToOne
	@JoinColumn(name = "id_assinatura_principal")
	private Assinatura assinaturaPrincipal;
	
	private LocalDateTime dataCadastro;

	private LocalDate dataVencimento;
	
	private boolean pausado;
	
	private String emailPagamento;
	
	private String telefone;
	
	private String documentoPagamento;
	
	private boolean desabilitada;
	
}
