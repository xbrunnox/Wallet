package br.app.grid.wallet.assinatura;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.robo.Robo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssinaturaExpert {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="id_assinatura")
	private Assinatura assinatura;
	
	@ManyToOne
	@JoinColumn(name="expert")
	private Robo expert;
	
	private Double volume;
	
	private Double volumeMaximo;
	
	private boolean ativado;

}
