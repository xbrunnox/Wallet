package br.app.grid.wallet.expert;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.app.grid.wallet.robo.Robo;

/**
 * <b>ExpertConfiguracao</b><br>
 * Entidade que representa a configuração de um expert.
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 15 de setembro de 2023.
 */
public class ExpertConfiguracao {
	
	@Id
	private Integer id;
	
	@ManyToOne
	private Robo expert;
	
	private String nome;
	
	private String valor;
	
	private String descricao;
	
	private ExpertConfiguracaoTipoEnum tipo;

}
