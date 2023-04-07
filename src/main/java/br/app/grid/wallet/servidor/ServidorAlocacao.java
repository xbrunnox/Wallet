package br.app.grid.wallet.servidor;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ServidorAlocacao {
	
	@Id
	private Integer id;
	
	private Integer total;

}
